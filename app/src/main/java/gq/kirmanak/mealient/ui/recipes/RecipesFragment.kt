package gq.kirmanak.mealient.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesBinding
import gq.kirmanak.mealient.extensions.collectWithViewLifecycle
import gq.kirmanak.mealient.extensions.refreshRequestFlow
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private val binding by viewBinding(FragmentRecipesBinding::bind)
    private val viewModel by viewModels<RecipeViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        activityViewModel.showLoginButton = true
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
        val adapter = RecipesPagingAdapter(viewModel, ::navigateToRecipeInfo)
        binding.recipes.adapter = adapter
        collectWithViewLifecycle(viewModel.pagingData) {
            Timber.v("setupRecipeAdapter: received data update")
            adapter.submitData(lifecycle, it)
        }
        collectWithViewLifecycle(adapter.onPagesUpdatedFlow) {
            Timber.v("setupRecipeAdapter: pages updated")
            binding.refresher.isRefreshing = false
        }
        collectWithViewLifecycle(binding.refresher.refreshRequestFlow()) {
            Timber.v("setupRecipeAdapter: received refresh request")
            adapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        // Prevent RV leaking through mObservers list in adapter
        binding.recipes.adapter = null
        activityViewModel.showLoginButton = false
    }
}