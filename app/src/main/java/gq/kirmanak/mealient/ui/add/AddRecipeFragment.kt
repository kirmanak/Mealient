package gq.kirmanak.mealient.ui.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.helper.widget.Flow
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.add.AddRecipeIngredientInfo
import gq.kirmanak.mealient.data.add.AddRecipeInstructionInfo
import gq.kirmanak.mealient.data.add.AddRecipeSettingsInfo
import gq.kirmanak.mealient.databinding.FragmentAddRecipeBinding
import gq.kirmanak.mealient.databinding.ViewSingleInputBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private val binding by viewBinding(FragmentAddRecipeBinding::bind)
    private val viewModel by viewModels<AddRecipeViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = false,
                checkedMenuItemId = R.id.add_recipe,
            )
        }
        viewModel.loadPreservedRequest()
        setupViews()
        observeAddRecipeResult()
    }

    private fun observeAddRecipeResult() {
        logger.v { "observeAddRecipeResult() called" }
        collectWhenViewResumed(viewModel.addRecipeResult, ::onRecipeSaveResult)
    }

    private fun onRecipeSaveResult(isSuccessful: Boolean) = with(binding) {
        logger.v { "onRecipeSaveResult() called with: isSuccessful = $isSuccessful" }

        listOf(clearButton, saveRecipeButton).forEach { it.isEnabled = true }

        val toastText = if (isSuccessful) {
            R.string.fragment_add_recipe_save_success
        } else {
            R.string.fragment_add_recipe_save_error
        }
        Toast.makeText(requireContext(), getString(toastText), Toast.LENGTH_SHORT).show()
    }

    private fun setupViews() = with(binding) {
        logger.v { "setupViews() called" }
        saveRecipeButton.setOnClickListener {
            recipeNameInput.checkIfInputIsEmpty(
                inputLayout = recipeNameInputLayout,
                lifecycleOwner = viewLifecycleOwner,
                stringId = R.string.fragment_add_recipe_name_error,
                logger = logger,
            ) ?: return@setOnClickListener

            listOf(saveRecipeButton, clearButton).forEach { it.isEnabled = false }

            viewModel.saveRecipe()
        }

        clearButton.setOnClickListener { viewModel.clear() }

        newIngredientButton.setOnClickListener {
            inflateInputRow(ingredientsFlow, R.string.fragment_add_recipe_ingredient_hint)
        }

        newInstructionButton.setOnClickListener {
            inflateInputRow(instructionsFlow, R.string.fragment_add_recipe_instruction_hint)
        }

        listOf(
            recipeNameInput,
            recipeDescriptionInput,
            recipeYieldInput
        ).forEach { it.doAfterTextChanged { saveValues() } }

        listOf(
            publicRecipe,
            disableComments
        ).forEach { it.setOnCheckedChangeListener { _, _ -> saveValues() } }

        collectWhenViewResumed(viewModel.preservedAddRecipeRequest, ::onSavedInputLoaded)
    }

    private fun inflateInputRow(flow: Flow, @StringRes hintId: Int, text: String? = null) {
        logger.v { "inflateInputRow() called with: flow = $flow, hintId = $hintId, text = $text" }
        val fragmentRoot = binding.holder
        val inputBinding = ViewSingleInputBinding.inflate(layoutInflater, fragmentRoot, false)
        val root = inputBinding.root
        root.setHint(hintId)
        val input = inputBinding.input
        input.setText(text)
        input.doAfterTextChanged { saveValues() }
        root.id = View.generateViewId()
        fragmentRoot.addView(root)
        flow.addView(root)
        root.setEndIconOnClickListener {
            flow.removeView(root)
            fragmentRoot.removeView(root)
        }
    }

    private fun saveValues() = with(binding) {
        logger.v { "saveValues() called" }
        val instructions = parseInputRows(instructionsFlow).map { AddRecipeInstructionInfo(it) }
        val ingredients = parseInputRows(ingredientsFlow).map { AddRecipeIngredientInfo(it) }
        val settings = AddRecipeSettingsInfo(
            public = publicRecipe.isChecked,
            disableComments = disableComments.isChecked,
        )
        viewModel.preserve(
            AddRecipeInfo(
                name = recipeNameInput.text.toString(),
                description = recipeDescriptionInput.text.toString(),
                recipeYield = recipeYieldInput.text.toString(),
                recipeIngredient = ingredients,
                recipeInstructions = instructions,
                settings = settings
            )
        )
    }

    private fun parseInputRows(flow: Flow): List<String> =
        flow.referencedIds.asSequence()
            .mapNotNull { binding.holder.findViewById(it) }
            .map { ViewSingleInputBinding.bind(it) }
            .map { it.input.text.toString() }
            .filterNot { it.isBlank() }
            .toList()

    private fun onSavedInputLoaded(request: AddRecipeInfo) = with(binding) {
        logger.v { "onSavedInputLoaded() called with: request = $request" }

        request.recipeIngredient.map { it.note }
            .showIn(ingredientsFlow, R.string.fragment_add_recipe_ingredient_hint)

        request.recipeInstructions.map { it.text }
            .showIn(instructionsFlow, R.string.fragment_add_recipe_instruction_hint)

        recipeNameInput.setText(request.name)
        recipeDescriptionInput.setText(request.description)
        recipeYieldInput.setText(request.recipeYield)
        publicRecipe.isChecked = request.settings.public
        disableComments.isChecked = request.settings.disableComments
    }

    private fun Iterable<String>.showIn(flow: Flow, @StringRes hintId: Int) {
        logger.v { "showIn() called with: flow = $flow, hintId = $hintId" }
        flow.removeAllViews()
        forEach { inflateInputRow(flow = flow, hintId = hintId, text = it) }
    }

    private fun Flow.removeAllViews() {
        logger.v { "removeAllViews() called" }
        for (id in referencedIds.iterator()) {
            val view = binding.holder.findViewById<View>(id) ?: continue
            removeView(view)
            binding.holder.removeView(view)
        }
    }
}