package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
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
        viewModel.uiStateLive.observe(this, ::onUiStateChange)
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        logger.v { "onNavigationItemSelected() called with: menuItem = $menuItem" }
        menuItem.isChecked = true
        val directions = when (menuItem.itemId) {
            R.id.add_recipe -> actionGlobalAddRecipeFragment()
            R.id.recipes_list -> actionGlobalRecipesListFragment()
            R.id.change_url -> actionGlobalBaseURLFragment()
            R.id.login -> actionGlobalAuthenticationFragment()
            R.id.logout -> {
                viewModel.logout()
                binding.drawer.close()
                return true
            }
            else -> throw IllegalArgumentException("Unknown menu item id: ${menuItem.itemId}")
        }
        navigateTo(directions)
        binding.drawer.close()
        return true
    }

    private fun onUiStateChange(uiState: MainActivityUiState) {
        logger.v { "onUiStateChange() called with: uiState = $uiState" }
        with(binding.navigationView) {
            isVisible = uiState.navigationVisible
            menu.findItem(R.id.logout).isVisible = uiState.canShowLogout
            menu.findItem(R.id.login).isVisible = uiState.canShowLogin
        }
        binding.toolbar.menu.findItem(R.id.search_recipe_action).apply {
            isVisible = uiState.searchVisible
            setupSearchItem(this)
        }
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

        searchView.setQuery(viewModel.lastSearchQuery, false)
        searchView.isIconified = viewModel.lastSearchQuery.isNullOrBlank()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logger.v { "onOptionsItemSelected() called with: item = $item" }
        val result = when (item.itemId) {
            R.id.login -> {
                navigateTo(actionGlobalAuthenticationFragment())
                true
            }
            R.id.logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return result
    }

    private fun navigateTo(directions: NavDirections) {
        logger.v { "navigateTo() called with: directions = $directions" }
        navController.navigate(directions)
    }
}