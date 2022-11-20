package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalAddRecipeFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalAuthenticationFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalBaseURLFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalRecipesListFragment
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.MainActivityBinding
import gq.kirmanak.mealient.extensions.isDarkThemeOn
import gq.kirmanak.mealient.extensions.observeOnce
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val binding: MainActivityBinding by viewBinding(MainActivityBinding::bind, R.id.drawer)
    private val viewModel by viewModels<MainActivityViewModel>()
    private val navController: NavController
        get() = binding.navHost.getFragment<NavHostFragment>().navController

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }
        splashScreen.setKeepOnScreenCondition { viewModel.startDestination.value == null }
        setContentView(binding.root)
        setupUi()
        configureNavGraph()
    }

    private fun configureNavGraph() {
        logger.v { "configureNavGraph() called" }
        viewModel.startDestination.observeOnce(this) {
            logger.d { "configureNavGraph: received destination" }
            val controller = navController
            val graph = controller.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(it)
            controller.setGraph(graph, intent.extras)
        }
    }

    private fun setupUi() {
        binding.toolbar.setNavigationOnClickListener { binding.drawer.open() }
        binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelected)
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            val isAppearanceLightBars = !isDarkThemeOn()
            isAppearanceLightNavigationBars = isAppearanceLightBars
            isAppearanceLightStatusBars = isAppearanceLightBars
        }
        setupSearchItem(binding.toolbar.menu.findItem(R.id.search_recipe_action))
        viewModel.uiStateLive.observe(this, ::onUiStateChange)
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        logger.v { "onNavigationItemSelected() called with: menuItem = $menuItem" }
        if (menuItem.isChecked) {
            logger.d { "Not navigating because it is the current destination" }
            binding.drawer.close()
            return true
        }
        val directions = when (menuItem.itemId) {
            R.id.add_recipe -> actionGlobalAddRecipeFragment()
            R.id.recipes_list -> actionGlobalRecipesListFragment()
            R.id.change_url -> actionGlobalBaseURLFragment(false)
            R.id.login -> actionGlobalAuthenticationFragment()
            R.id.logout -> {
                viewModel.logout()
                return true
            }
            else -> throw IllegalArgumentException("Unknown menu item id: ${menuItem.itemId}")
        }
        menuItem.isChecked = true
        navigateTo(directions)
        return true
    }

    private fun onUiStateChange(uiState: MainActivityUiState) {
        logger.v { "onUiStateChange() called with: uiState = $uiState" }
        for (menuItem in binding.navigationView.menu.iterator()) {
            val itemId = menuItem.itemId
            when (itemId) {
                R.id.logout -> menuItem.isVisible = uiState.canShowLogout
                R.id.login -> menuItem.isVisible = uiState.canShowLogin
            }
            menuItem.isChecked = itemId == uiState.checkedMenuItemId
        }

        if (uiState.navigationVisible) {
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.toolbar.setNavigationIcon(R.drawable.ic_menu)
        } else {
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            binding.toolbar.navigationIcon = null
        }

        binding.toolbar.menu.findItem(R.id.search_recipe_action).isVisible = uiState.searchVisible
    }

    private fun setupSearchItem(searchItem: MenuItem) {
        logger.v { "setupSearchItem() called with: searchItem = $searchItem" }
        val searchView = searchItem.actionView as? SearchView
        if (searchView == null) {
            logger.e { "setupSearchItem: search item's actionView is null or not SearchView" }
            return
        }

        searchView.queryHint = getString(R.string.search_recipes_hint)
        searchView.isSubmitButtonEnabled = false
        searchView.setIconifiedByDefault(false)

        searchView.setOnCloseListener {
            logger.v { "onClose() called" }
            viewModel.onSearchQuery(null)
            false
        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                logger.v { "onQueryTextChange() called with: newText = $newText" }
                viewModel.onSearchQuery(newText?.trim()?.takeUnless { it.isEmpty() })
                return true
            }
        })
    }

    private fun navigateTo(directions: NavDirections) {
        logger.v { "navigateTo() called with: directions = $directions" }
        binding.toolbarHolder.setExpanded(true)
        binding.drawer.close()
        navController.navigate(directions)
    }
}