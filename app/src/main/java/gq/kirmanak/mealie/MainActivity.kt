package gq.kirmanak.mealie

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealie.databinding.MainActivityBinding
import gq.kirmanak.mealie.ui.auth.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest
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
        listenToAuthStatuses()
    }

    private fun listenToAuthStatuses() {
        Timber.v("listenToAuthStatuses() called")
        lifecycleScope.launchWhenCreated {
            authViewModel.authenticationStatuses().collectLatest {
                changeAuthStatus(it)
            }
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
}