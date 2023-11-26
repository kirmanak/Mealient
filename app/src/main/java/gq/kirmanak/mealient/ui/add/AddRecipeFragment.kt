package gq.kirmanak.mealient.ui.add

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.ui.BaseComposeFragment
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel

@AndroidEntryPoint
internal class AddRecipeFragment : BaseComposeFragment() {

    private val viewModel by viewModels<AddRecipeViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.AddRecipe,
            )
        }
    }

    @Composable
    override fun Screen() {
        val screenState by viewModel.screenState.collectAsState()

        AddRecipeScreen(
            state = screenState,
            onEvent = viewModel::onEvent,
        )
    }

}