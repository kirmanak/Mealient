package gq.kirmanak.mealient.ui.disclaimer

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.ui.BaseComposeFragment
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel

@AndroidEntryPoint
internal class DisclaimerFragment : BaseComposeFragment() {

    private val viewModel by viewModels<DisclaimerViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startCountDown()
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = false,
                searchVisible = false,
                checkedMenuItem = null
            )
        }
        collectWhenViewResumed(viewModel.isAcceptedState, ::onAcceptStateChange)
    }

    private fun onAcceptStateChange(isAccepted: Boolean) {
        logger.v { "onAcceptStateChange() called with: isAccepted = $isAccepted" }
        if (isAccepted) navigateNext()
    }

    private fun navigateNext() {
        logger.v { "navigateNext() called" }
        findNavController().navigate(
            DisclaimerFragmentDirections.actionDisclaimerFragmentToBaseURLFragment(true)
        )
    }

    @Composable
    override fun Screen() {
        val screenState by viewModel.screenState.collectAsState()

        DisclaimerScreen(
            state = screenState,
            onButtonClick = viewModel::acceptDisclaimer,
        )
    }
}