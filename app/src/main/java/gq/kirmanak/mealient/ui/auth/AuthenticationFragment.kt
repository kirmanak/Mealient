package gq.kirmanak.mealient.ui.auth

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
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel

@AndroidEntryPoint
class AuthenticationFragment : BaseComposeFragment() {

    private val viewModel by viewModels<AuthenticationViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Composable
    override fun Screen() {
        val screenState by viewModel.screenState.collectAsState()

        AuthenticationScreen(
            screenState = screenState,
            onEvent = viewModel::onEvent,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.Login
            )
        }
        collectWhenViewResumed(viewModel.screenState, ::onScreenStateChange)
    }

    private fun onScreenStateChange(screenState: AuthenticationScreenState) {
        logger.v { "onScreenStateChange() called with: screenState = $screenState" }
        if (screenState.isSuccessful) {
            findNavController().navigateUp()
        }
    }
}