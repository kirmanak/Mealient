package gq.kirmanak.mealient.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalAddRecipeFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalAuthenticationFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalBaseURLFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalRecipesListFragment
import gq.kirmanak.mealient.NavGraphDirections.Companion.actionGlobalShoppingListsFragment
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.MainActivityBinding
import gq.kirmanak.mealient.extensions.collectWhenResumed
import gq.kirmanak.mealient.extensions.observeOnce
import gq.kirmanak.mealient.logging.getLogFile
import gq.kirmanak.mealient.ui.ActivityUiState
import gq.kirmanak.mealient.ui.BaseActivity
import gq.kirmanak.mealient.ui.CheckableMenuItem

private const val EMAIL_FOR_LOGS = "mealient@gmail.com"

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityBinding>(
    binder = MainActivityBinding::bind,
    containerId = R.id.drawer,
    layoutRes = R.layout.main_activity,
) {

    private val viewModel by viewModels<MainActivityViewModel>()
    private val navController: NavController
        get() = binding.navHost.getFragment<NavHostFragment>().navController

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.startDestination.value == null }
        setupUi()
        configureNavGraph()
    }

    private fun configureNavGraph() {
        logger.v { "configureNavGraph() called" }
        viewModel.startDestination.observeOnce(this) {
            logger.d { "configureNavGraph: received destination" }
            val controller = navController
            val graph = controller.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(it.startDestinationId)
            controller.setGraph(graph, it.startDestinationArgs)
        }
    }

    private fun setupUi() {
        binding.toolbar.setNavigationOnClickListener {
            binding.toolbar.clearSearchFocus()
            binding.drawer.open()
        }
        binding.toolbar.onSearchQueryChanged { query ->
            viewModel.onSearchQuery(query.trim().takeUnless { it.isEmpty() })
        }
        binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelected)
        collectWhenResumed(viewModel.uiState, ::onUiStateChange)
        collectWhenResumed(viewModel.clearSearchViewFocus) {
            logger.d { "clearSearchViewFocus(): received event" }
            binding.toolbar.clearSearchFocus()
        }
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
            R.id.shopping_lists -> actionGlobalShoppingListsFragment()
            R.id.change_url -> actionGlobalBaseURLFragment(false)
            R.id.login -> actionGlobalAuthenticationFragment()
            R.id.logout -> {
                viewModel.logout()
                return true
            }

            R.id.email_logs -> {
                emailLogs()
                return true
            }

            else -> throw IllegalArgumentException("Unknown menu item id: ${menuItem.itemId}")
        }
        menuItem.isChecked = true
        navigateTo(directions)
        return true
    }

    private fun emailLogs() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.activity_main_email_logs_confirmation_message)
            .setTitle(R.string.activity_main_email_logs_confirmation_title)
            .setPositiveButton(R.string.activity_main_email_logs_confirmation_positive) { _, _ -> doEmailLogs() }
            .setNegativeButton(R.string.activity_main_email_logs_confirmation_negative, null)
            .show()
    }

    private fun doEmailLogs() {
        val logFileUri = try {
            FileProvider.getUriForFile(this, "$packageName.provider", getLogFile())
        } catch (e: Exception) {
            return
        }
        val emailIntent = buildIntent(logFileUri)
        val chooserIntent = Intent.createChooser(emailIntent, null)
        startActivity(chooserIntent)
    }

    private fun buildIntent(logFileUri: Uri?): Intent {
        val emailIntent = Intent(Intent.ACTION_SEND)
        val to = arrayOf(EMAIL_FOR_LOGS)
        emailIntent.setType("text/plain")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        emailIntent.putExtra(Intent.EXTRA_STREAM, logFileUri)
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.activity_main_email_logs_subject)
        )
        return emailIntent
    }

    private fun onUiStateChange(uiState: ActivityUiState) {
        logger.v { "onUiStateChange() called with: uiState = $uiState" }
        val checkedMenuItem = when (uiState.checkedMenuItem) {
            CheckableMenuItem.ShoppingLists -> R.id.shopping_lists
            CheckableMenuItem.RecipesList -> R.id.recipes_list
            CheckableMenuItem.AddRecipe -> R.id.add_recipe
            CheckableMenuItem.ChangeUrl -> R.id.change_url
            CheckableMenuItem.Login -> R.id.login
            null -> null
        }
        for (menuItem in binding.navigationView.menu.iterator()) {
            val itemId = menuItem.itemId
            when (itemId) {
                R.id.logout -> menuItem.isVisible = uiState.canShowLogout
                R.id.login -> menuItem.isVisible = uiState.canShowLogin
            }
            menuItem.isChecked = itemId == checkedMenuItem
        }

        binding.toolbar.isVisible = uiState.navigationVisible
        binding.root.setDrawerLockMode(
            if (uiState.navigationVisible) {
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
        )

        binding.toolbar.isSearchVisible = uiState.searchVisible

        if (uiState.searchVisible) {
            binding.toolbar.setBackgroundResource(R.drawable.bg_toolbar)
        } else {
            binding.toolbar.background = null
        }
    }

    private fun navigateTo(directions: NavDirections) {
        logger.v { "navigateTo() called with: directions = $directions" }
        binding.drawer.close()
        navController.navigate(directions)
    }
}