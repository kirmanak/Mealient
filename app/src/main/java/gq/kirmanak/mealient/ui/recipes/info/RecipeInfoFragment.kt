package gq.kirmanak.mealient.ui.recipes.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.databinding.FragmentRecipeInfoBinding
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import javax.inject.Inject

@AndroidEntryPoint
class RecipeInfoFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentRecipeInfoBinding::bind)
    private val viewModel by viewModels<RecipeInfoViewModel>()
    private lateinit var ingredientsAdapter: RecipeIngredientsAdapter

    @Inject
    lateinit var instructionsAdapter: RecipeInstructionsAdapter

    @Inject
    lateinit var recipeIngredientsAdapterFactory: RecipeIngredientsAdapter.Factory

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var recipeImageLoader: RecipeImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.v { "onCreateView() called" }
        return FragmentRecipeInfoBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called" }

        with(binding) {
            instructionsList.adapter = instructionsAdapter
        }

        with(viewModel) {
            uiState.observe(viewLifecycleOwner, ::onUiStateChange)
        }
    }

    private fun onUiStateChange(uiState: RecipeInfoUiState) = with(binding) {
        logger.v { "onUiStateChange() called" }
        if (::ingredientsAdapter.isInitialized.not()) {
            ingredientsAdapter = recipeIngredientsAdapterFactory.build(uiState.disableAmounts)
            ingredientsList.adapter = ingredientsAdapter
        }
        ingredientsHolder.isVisible = uiState.showIngredients
        instructionsGroup.isVisible = uiState.showInstructions
        recipeImageLoader.loadRecipeImage(image, uiState.summaryEntity)
        title.text = uiState.title
        description.text = uiState.description
        ingredientsAdapter.submitList(uiState.recipeIngredients)
        instructionsAdapter.submitList(uiState.recipeInstructions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.v { "onDestroyView() called" }
        // Prevent RV leaking through mObservers list in adapter
        with(binding) {
            ingredientsList.adapter = null
            instructionsList.adapter = null
        }
    }
}
