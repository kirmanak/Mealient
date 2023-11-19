package gq.kirmanak.mealient.ui.recipes.info

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
class RecipeInfoFragment : BaseComposeFragment() {

    private val viewModel by viewModels<RecipeInfoViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Composable
    override fun Screen() {
        val uiState by viewModel.uiState.collectAsState()
        RecipeScreen(uiState = uiState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = false,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.RecipesList,
            )
        }
    }
}
