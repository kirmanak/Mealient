package gq.kirmanak.mealient.ui.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.NavGraphDirections
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.MainActivityBinding
import gq.kirmanak.mealient.extensions.observeOnce
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val binding: MainActivityBinding by viewBinding(MainActivityBinding::bind, R.id.drawer)
    private val viewModel by viewModels<MainActivityViewModel>()
    private val title: String by lazy { getString(R.string.app_name) }
    private val uiState: MainActivityUiState get() = viewModel.uiState
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
        configureToolbar()
        configureNavGraph()
        viewModel.uiStateLive.observe(this, ::onUiStateChange)
        binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelected)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logger.v { "onNewIntent() called with: intent = $intent" }
        when (intent?.action) {
            Intent.ACTION_SEARCH -> onNewSearch(intent)
            else -> logger.w { "Unexpected intent!" }
        }
    }

    private fun onNewSearch(intent: Intent) {
        logger.v { "onNewSearch() called with: intent = $intent" }
        val query = intent.getStringExtra(SearchManager.QUERY)
        viewModel.onSearchQuery(query)
    }

    private fun configureNavGraph() {
        logger.v { "configureNavGraph() called" }
        viewModel.startDestination.observeOnce(this) {
            logger.d { "configureNavGraph: received destination" }
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(it)
            navController.setGraph(graph, intent.extras)
        }
    }

    private fun configureToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar)
        binding.toolbar.setNavigationOnClickListener { binding.drawer.open() }
        setToolbarRoundCorner()
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        logger.v { "onNavigationItemSelected() called with: menuItem = $menuItem" }
        menuItem.isChecked = true
        val directions = when (menuItem.itemId) {
            R.id.add_recipe -> NavGraphDirections.actionGlobalAddRecipeFragment()
            R.id.recipes_list -> NavGraphDirections.actionGlobalRecipesFragment()
            R.id.change_url -> NavGraphDirections.actionGlobalBaseURLFragment()
            else -> throw IllegalArgumentException("Unknown menu item id: ${menuItem.itemId}")
        }
        navigateTo(directions)
        binding.drawer.close()
        return true
    }

    private fun onUiStateChange(uiState: MainActivityUiState) {
        logger.v { "onUiStateChange() called with: uiState = $uiState" }
        supportActionBar?.title = if (uiState.titleVisible) title else null
        binding.navigationView.isVisible = uiState.navigationVisible
        invalidateOptionsMenu()
    }

    private fun setToolbarRoundCorner() {
        logger.v { "setToolbarRoundCorner() called" }
        val drawables = listOf(
            binding.toolbarHolder.background as? MaterialShapeDrawable,
            binding.toolbar.background as? MaterialShapeDrawable,
        )
        logger.d { "setToolbarRoundCorner: drawables = $drawables" }
        val radius = resources.getDimension(R.dimen.main_activity_toolbar_corner_radius)
        for (drawable in drawables) {
            drawable?.apply {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radius).build()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        logger.v { "onCreateOptionsMenu() called with: menu = $menu" }
        menuInflater.inflate(R.menu.main_toolbar, menu)
        menu.findItem(R.id.logout).isVisible = uiState.canShowLogout
        menu.findItem(R.id.login).isVisible = uiState.canShowLogin
        val searchItem = menu.findItem(R.id.search_recipe_action)
        searchItem.isVisible = uiState.searchVisible
        val searchManager: SearchManager? = getSystemService()
        val searchView = searchItem.actionView as? SearchView
        if (searchManager != null && searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        } else {
            logger.e { "onCreateOptionsMenu: either search manager or search view is null" }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logger.v { "onOptionsItemSelected() called with: item = $item" }
        val result = when (item.itemId) {
            R.id.login -> {
                navigateTo(NavGraphDirections.actionGlobalAuthenticationFragment())
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