package gq.kirmanak.mealient.ui.recipes.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RecipeInfoFragment : Fragment() {

    private val viewModel by viewModels<RecipeInfoViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.v { "onCreateView() called" }
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()
                AppTheme {
                    RecipeScreen(uiState = uiState)
                }
            }
        }
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
