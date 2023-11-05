package gq.kirmanak.mealient.ui.recipes.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class RecipeInfoFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<RecipeInfoViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.v { "onCreateView() called" }
        return ComposeView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called" }
        (view as ComposeView).setContent {
            val uiState by viewModel.uiState.collectAsState()
            AppTheme {
                RecipeScreen(uiState = uiState)
            }
        }
    }
}
