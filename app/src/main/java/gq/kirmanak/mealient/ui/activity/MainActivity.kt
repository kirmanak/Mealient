package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.MainActivityBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewModel by viewModels<MainActivityViewModel>()
    private val title: String by lazy { getString(R.string.app_name) }
    private val uiState: MainActivityUiState get() = viewModel.uiState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar)
        binding.toolbar.setNavigationOnClickListener { binding.drawer.open() }
        setToolbarRoundCorner()
        viewModel.uiStateLive.observe(this, ::onUiStateChange)
        binding.navigationView.setNavigationItemSelectedListener(::onNavigationItemSelected)
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        Timber.v("onNavigationItemSelected() called with: menuItem = $menuItem")
        menuItem.isChecked = true
        val deepLink = when (menuItem.itemId) {
            R.id.add_recipe -> ADD_RECIPE_DEEP_LINK
            R.id.recipes_list -> RECIPES_LIST_DEEP_LINK
            else -> throw IllegalArgumentException("Unknown menu item id: ${menuItem.itemId}")
        }
        navigateDeepLink(deepLink)
        binding.drawer.close()
        return true
    }

    private fun onUiStateChange(uiState: MainActivityUiState) {
        Timber.v("onUiStateChange() called with: uiState = $uiState")
        supportActionBar?.title = if (uiState.titleVisible) title else null
        binding.navigationView.isVisible = uiState.navigationVisible
        invalidateOptionsMenu()
    }

    private fun setToolbarRoundCorner() {
        Timber.v("setToolbarRoundCorner() called")
        val drawables = listOf(
            binding.toolbarHolder.background as? MaterialShapeDrawable,
            binding.toolbar.background as? MaterialShapeDrawable,
        )
        Timber.d("setToolbarRoundCorner: drawables = $drawables")
        val radius = resources.getDimension(R.dimen.main_activity_toolbar_corner_radius)
        for (drawable in drawables) {
            drawable?.apply {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radius).build()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.v("onCreateOptionsMenu() called with: menu = $menu")
        menuInflater.inflate(R.menu.main_toolbar, menu)
        menu.findItem(R.id.logout).isVisible = uiState.canShowLogout
        menu.findItem(R.id.login).isVisible = uiState.canShowLogin
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.v("onOptionsItemSelected() called with: item = $item")
        val result = when (item.itemId) {
            R.id.login -> {
                navigateDeepLink(AUTH_DEEP_LINK)
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

    private fun navigateDeepLink(deepLink: String) {
        Timber.v("navigateDeepLink() called with: deepLink = $deepLink")
        findNavController(binding.navHost.id).navigate(deepLink.toUri())
    }

    companion object {
        private const val AUTH_DEEP_LINK = "mealient://authenticate"
        private const val ADD_RECIPE_DEEP_LINK = "mealient://recipe/add"
        private const val RECIPES_LIST_DEEP_LINK = "mealient://recipe/list"
    }
}