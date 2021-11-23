package gq.kirmanak.mealient.ui.recipes.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.databinding.FragmentRecipeInfoBinding
import gq.kirmanak.mealient.ui.auth.AuthenticationViewModel
import timber.log.Timber

@AndroidEntryPoint
class RecipeInfoFragment : Fragment() {
    private var _binding: FragmentRecipeInfoBinding? = null
    private val binding: FragmentRecipeInfoBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val arguments by navArgs<RecipeInfoFragmentArgs>()
    private val viewModel by viewModels<RecipeInfoViewModel>()

    private val authViewModel by viewModels<AuthenticationViewModel>()
    private val authStatuses by lazy { authViewModel.authenticationStatuses() }
    private val authStatusObserver = Observer<Boolean> { onAuthStatusChange(it) }
    private fun onAuthStatusChange(isAuthenticated: Boolean) {
        Timber.v("onAuthStatusChange() called with: isAuthenticated = $isAuthenticated")
        if (!isAuthenticated) {
            authStatuses.removeObserver(authStatusObserver)
            navigateToAuthFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        _binding = FragmentRecipeInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        authStatuses.observe(this, authStatusObserver)

        viewModel.loadRecipeImage(binding.image, arguments.recipeSlug)
        viewModel.loadRecipeInfo(arguments.recipeId, arguments.recipeSlug)

        viewModel.recipeInfo.observe(viewLifecycleOwner) {
            Timber.d("onViewCreated: full info $it")
            binding.title.text = it.recipeSummaryEntity.name
            binding.description.text = it.recipeSummaryEntity.description

            val recipeIngredientsAdapter = RecipeIngredientsAdapter()
            binding.ingredientsList.adapter = recipeIngredientsAdapter
            binding.ingredientsList.layoutManager = LinearLayoutManager(requireContext())
            recipeIngredientsAdapter.submitList(it.recipeIngredients)

            val recipeInstructionsAdapter = RecipeInstructionsAdapter()
            binding.instructionsList.adapter = recipeInstructionsAdapter
            binding.instructionsList.layoutManager = LinearLayoutManager(requireContext())
            recipeInstructionsAdapter.submitList(it.recipeInstructions)
        }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = null
    }

    private fun navigateToAuthFragment() {
        Timber.v("navigateToAuthFragment() called")
        findNavController().navigate(RecipeInfoFragmentDirections.actionRecipeInfoFragmentToAuthenticationFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        _binding = null
    }
}