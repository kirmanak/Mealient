package gq.kirmanak.mealient.shopping_lists.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.ui.ActivityUiStateController
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.CheckableMenuItem
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListsFragment : Fragment() {

    @Inject
    lateinit var activityUiStateController: ActivityUiStateController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    MealientApp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityUiStateController.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.ShoppingLists,
            )
        }
    }
}


