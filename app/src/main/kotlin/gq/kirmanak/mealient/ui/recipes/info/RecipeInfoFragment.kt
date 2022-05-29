package gq.kirmanak.mealient.ui.recipes.info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentRecipeInfoBinding
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RecipeInfoFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentRecipeInfoBinding::bind)
    private val arguments by navArgs<RecipeInfoFragmentArgs>()
    private val viewModel by viewModels<RecipeInfoViewModel>()
    private val ingredientsAdapter = RecipeIngredientsAdapter()
    private val instructionsAdapter = RecipeInstructionsAdapter()

    @Inject
    lateinit var recipeImageLoader: RecipeImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called")
        return FragmentRecipeInfoBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called")

        with(binding) {
            ingredientsList.adapter = ingredientsAdapter
            instructionsList.adapter = instructionsAdapter
        }

        with(viewModel) {
            loadRecipeInfo(arguments.recipeId, arguments.recipeSlug)
            uiState.observe(viewLifecycleOwner, ::onUiStateChange)
        }
    }

    private fun onUiStateChange(uiState: RecipeInfoUiState) = with(binding) {
        Timber.v("onUiStateChange() called")
        ingredientsHolder.isVisible = uiState.areIngredientsVisible
        instructionsGroup.isVisible = uiState.areInstructionsVisible
        uiState.recipeInfo?.let {
            recipeImageLoader.loadRecipeImage(image, it.recipeSummaryEntity)
            title.text = it.recipeSummaryEntity.name
            description.text = it.recipeSummaryEntity.description
            ingredientsAdapter.submitList(it.recipeIngredients)
            instructionsAdapter.submitList(it.recipeInstructions)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), R.style.NoShapeBottomSheetDialog)

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        // Prevent RV leaking through mObservers list in adapter
        with(binding) {
            ingredientsList.adapter = null
            instructionsList.adapter = null
        }
    }
}
