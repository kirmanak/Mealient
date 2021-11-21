package gq.kirmanak.mealient

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.databinding.MainActivityBinding
import gq.kirmanak.mealient.ui.auth.AuthenticationViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val authViewModel by viewModels<AuthenticationViewModel>()
    private var isAuthenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setToolbarRoundCorner()
        listenToAuthStatuses()
    }

    private fun setToolbarRoundCorner() {
        Timber.v("setToolbarRoundCorner() called")
        val drawables = listOf(
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
        authViewModel.authenticationStatuses().observe(this) {
            changeAuthStatus(it)
        }
    }

    private fun changeAuthStatus(it: Boolean) {
        Timber.v("changeAuthStatus() called with: it = $it")
        if (isAuthenticated == it) return
        isAuthenticated = it
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.v("onCreateOptionsMenu() called with: menu = $menu")
        menuInflater.inflate(R.menu.main_toolbar, menu)
        menu.findItem(R.id.logout).isVisible = isAuthenticated
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.v("onOptionsItemSelected() called with: item = $item")
        val result = if (item.itemId == R.id.logout) {
            authViewModel.logout()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
        return result
    }
}