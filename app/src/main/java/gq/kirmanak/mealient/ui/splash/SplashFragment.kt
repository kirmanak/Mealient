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
import gq.kirmanak.mealient.ui.setActionBarVisibility
import gq.kirmanak.mealient.ui.setSystemUiVisibility
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
        changeFullscreenState(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        changeFullscreenState(false)
    }

    private fun changeFullscreenState(isFullscreen: Boolean) {
        Timber.v("changeFullscreenState() called with: isFullscreen = $isFullscreen")
        (activity as? AppCompatActivity)?.setActionBarVisibility(!isFullscreen)
        activity?.setSystemUiVisibility(!isFullscreen)
    }
}