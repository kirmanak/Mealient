package gq.kirmanak.mealient.ui.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.extensions.setActionBarVisibility
import gq.kirmanak.mealient.extensions.setSystemUiVisibility
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }
        viewModel.nextDestination.observe(this, ::onNextDestination)
    }

    private fun onNextDestination(navDirections: NavDirections) {
        logger.v { "onNextDestination() called with: navDirections = $navDirections" }
        findNavController().navigate(navDirections)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        changeFullscreenState(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.v { "onDestroyView() called" }
        changeFullscreenState(false)
    }

    private fun changeFullscreenState(isFullscreen: Boolean) {
        logger.v { "changeFullscreenState() called with: isFullscreen = $isFullscreen" }
        (activity as? AppCompatActivity)?.setActionBarVisibility(!isFullscreen, logger)
        activity?.setSystemUiVisibility(!isFullscreen, logger)
    }
}