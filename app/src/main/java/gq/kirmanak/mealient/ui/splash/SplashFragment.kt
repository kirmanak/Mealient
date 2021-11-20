package gq.kirmanak.mealient.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.databinding.FragmentSplashBinding
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        viewModel.nextDestination.observe(this) {
            Timber.d("onCreate: next destination $it")
            findNavController().navigate(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume() called")
        changeFullscreenState(true)
    }

    override fun onPause() {
        super.onPause()
        Timber.v("onPause() called")
        changeFullscreenState(false)
    }

    private fun changeFullscreenState(isFullscreen: Boolean) {
        Timber.v("changeFullscreenState() called with: isFullscreen = $isFullscreen")

        val supportActionBar = (activity as? AppCompatActivity)?.supportActionBar
        Timber.d("changeFullscreenState: action bar = $supportActionBar")
        if (isFullscreen) supportActionBar?.hide()
        else supportActionBar?.show()

        val decorView = activity?.window?.decorView
        Timber.d("changeFullscreenState: decorView = $decorView")
        decorView?.systemUiVisibility = if (isFullscreen) View.SYSTEM_UI_FLAG_FULLSCREEN else 0
    }
}