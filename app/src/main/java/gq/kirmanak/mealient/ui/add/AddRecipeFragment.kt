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
import gq.kirmanak.mealient.data.add.models.AddRecipeIngredient
import gq.kirmanak.mealient.data.add.models.AddRecipeInstruction
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.data.add.models.AddRecipeSettings
import gq.kirmanak.mealient.databinding.FragmentAddRecipeBinding
import gq.kirmanak.mealient.databinding.ViewSingleInputBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import timber.log.Timber

@AndroidEntryPoint
class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    private val binding by viewBinding(FragmentAddRecipeBinding::bind)
    private val viewModel by viewModels<AddRecipeViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        activityViewModel.updateUiState {
            it.copy(loginButtonVisible = true, titleVisible = false, navigationVisible = true)
        }
        viewModel.loadPreservedRequest()
        setupViews()
        observeAddRecipeResult()
    }

    private fun observeAddRecipeResult() {
        Timber.v("observeAddRecipeResult() called")
        collectWhenViewResumed(viewModel.addRecipeResult, ::onRecipeSaveResult)
    }

    private fun onRecipeSaveResult(isSuccessful: Boolean) = with(binding) {
        Timber.v("onRecipeSaveResult() called with: isSuccessful = $isSuccessful")

        listOf(clearButton, saveRecipeButton).forEach { it.isEnabled = true }

        val toastText = if (isSuccessful) {
            R.string.fragment_add_recipe_save_success
        } else {
            R.string.fragment_add_recipe_save_error
        }
        Toast.makeText(requireContext(), getString(toastText), Toast.LENGTH_SHORT).show()
    }

    private fun setupViews() = with(binding) {
        Timber.v("setupViews() called")
        saveRecipeButton.setOnClickListener {
            recipeNameInput.checkIfInputIsEmpty(
                inputLayout = recipeNameInputLayout,
                lifecycleOwner = viewLifecycleOwner,
                stringId = R.string.fragment_add_recipe_name_error
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
        Timber.v("inflateInputRow() called with: flow = $flow, hintId = $hintId, text = $text")
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
        Timber.v("saveValues() called")
        val instructions = parseInputRows(instructionsFlow).map { AddRecipeInstruction(text = it) }
        val ingredients = parseInputRows(ingredientsFlow).map { AddRecipeIngredient(note = it) }
        val settings = AddRecipeSettings(
            public = publicRecipe.isChecked,
            disableComments = disableComments.isChecked,
        )
        viewModel.preserve(
            AddRecipeRequest(
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

    private fun onSavedInputLoaded(request: AddRecipeRequest) = with(binding) {
        Timber.v("onSavedInputLoaded() called with: request = $request")
        recipeNameInput.setText(request.name)
        recipeDescriptionInput.setText(request.description)
        recipeYieldInput.setText(request.recipeYield)
        publicRecipe.isChecked = request.settings.public
        disableComments.isChecked = request.settings.disableComments

        request.recipeIngredient.map { it.note }
            .showIn(ingredientsFlow, R.string.fragment_add_recipe_ingredient_hint)

        request.recipeInstructions.map { it.text }
            .showIn(instructionsFlow, R.string.fragment_add_recipe_instruction_hint)
    }

    private fun Iterable<String>.showIn(flow: Flow, @StringRes hintId: Int) {
        Timber.v("showIn() called with: flow = $flow, hintId = $hintId")
        flow.removeAllViews()
        forEach { inflateInputRow(flow = flow, hintId = hintId, text = it) }
    }

    private fun Flow.removeAllViews() {
        Timber.v("removeAllViews() called")
        for (id in referencedIds.iterator()) {
            val view = binding.holder.findViewById<View>(id) ?: continue
            removeView(view)
            binding.holder.removeView(view)
        }
    }
}