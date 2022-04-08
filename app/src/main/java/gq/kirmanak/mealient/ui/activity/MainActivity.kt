package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.findNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.MainActivityBinding
import gq.kirmanak.mealient.ui.auth.AuthenticationState
import gq.kirmanak.mealient.ui.auth.AuthenticationState.AUTHORIZED
import gq.kirmanak.mealient.ui.auth.AuthenticationState.UNAUTHORIZED
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewModel by viewModels<MainActivityViewModel>()
    private var lastAuthenticationState: AuthenticationState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.drawable.ic_toolbar)
        setToolbarRoundCorner()
        listenToAuthStatuses()
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
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                    .build()
            }
        }
    }

    private fun listenToAuthStatuses() {
        Timber.v("listenToAuthStatuses() called")
        viewModel.authenticationStateLive.observe(this, ::onAuthStateUpdate)
    }

    private fun onAuthStateUpdate(authState: AuthenticationState) {
        Timber.v("onAuthStateUpdate() called with: it = $authState")
        lastAuthenticationState = authState
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.v("onCreateOptionsMenu() called with: menu = $menu")
        menuInflater.inflate(R.menu.main_toolbar, menu)
        menu.findItem(R.id.logout).isVisible = lastAuthenticationState == AUTHORIZED
        menu.findItem(R.id.login).isVisible = lastAuthenticationState == UNAUTHORIZED
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.v("onOptionsItemSelected() called with: item = $item")
        val result = when (item.itemId) {
            R.id.login -> {
                navigateToLogin()
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

    private fun navigateToLogin() {
        Timber.v("navigateToLogin() called")
        findNavController(binding.navHost.id).navigate("mealient://authenticate".toUri())
    }
}