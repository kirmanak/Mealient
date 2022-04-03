package gq.kirmanak.mealient.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesBinding
import gq.kirmanak.mealient.ui.refreshesLiveData
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private val binding by viewBinding(FragmentRecipesBinding::bind)
    private val viewModel by viewModels<RecipeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        setupRecipeAdapter()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = null
    }

    private fun navigateToRecipeInfo(recipeSummaryEntity: RecipeSummaryEntity) {
        Timber.v("navigateToRecipeInfo() called with: recipeSummaryEntity = $recipeSummaryEntity")
        findNavController().navigate(
            RecipesFragmentDirections.actionRecipesFragmentToRecipeInfoFragment(
                recipeSlug = recipeSummaryEntity.slug,
                recipeId = recipeSummaryEntity.remoteId
            )
        )
    }

    private fun setupRecipeAdapter() {
        Timber.v("setupRecipeAdapter() called")
        binding.recipes.adapter = viewModel.adapter
        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            Timber.d("setupRecipeAdapter: isRefreshing = $it")
            binding.refresher.isRefreshing = it
        }
        binding.refresher.refreshesLiveData().observe(viewLifecycleOwner) {
            Timber.d("setupRecipeAdapter: received refresh request")
            viewModel.adapter.refresh()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.nextRecipeInfo.collect {
                Timber.d("setupRecipeAdapter: navigating to recipe $it")
                navigateToRecipeInfo(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        // Prevent RV leaking through mObservers list in adapter
        binding.recipes.adapter = null
    }
}