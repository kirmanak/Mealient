package gq.kirmanak.mealient.ui.baseurl

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.ui.BaseComposeFragment
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel

@AndroidEntryPoint
internal class BaseURLFragment : BaseComposeFragment() {

    private val viewModel by viewModels<BaseURLViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val args by navArgs<BaseURLFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = !args.isOnboarding,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.ChangeUrl,
            )
        }
        collectWhenViewResumed(viewModel.screenState) {
            if (it.isConfigured) {
                findNavController().navigate(BaseURLFragmentDirections.actionBaseURLFragmentToRecipesListFragment())
            }
        }
    }

    @Composable
    override fun Screen() {
        val screenState by viewModel.screenState.collectAsState()

        BaseURLScreen(
            state = screenState,
            onEvent = viewModel::onEvent,
        )
    }
}