package gq.kirmanak.mealient.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesBinding
import gq.kirmanak.mealient.ui.SwipeRefreshLayoutHelper.listenToRefreshRequests
import gq.kirmanak.mealient.ui.auth.AuthenticationViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding: FragmentRecipesBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val viewModel by viewModels<RecipeViewModel>()
    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        setupRecipeAdapter()
        listenToAuthStatuses()
    }

    private fun navigateToRecipeInfo(recipeSummaryEntity: RecipeSummaryEntity) {
        findNavController().navigate(
            RecipesFragmentDirections.actionRecipesFragmentToRecipeInfoFragment(
                recipeSlug = recipeSummaryEntity.slug,
                recipeId = recipeSummaryEntity.remoteId
            )
        )
    }

    private fun listenToAuthStatuses() {
        Timber.v("listenToAuthStatuses() called")
        authViewModel.authenticationStatuses().observe(this) {
            Timber.v("listenToAuthStatuses: new auth status = $it")
            if (!it) navigateToAuthFragment()
        }
    }

    private fun navigateToAuthFragment() {
        Timber.v("navigateToAuthFragment() called")
        findNavController().navigate(RecipesFragmentDirections.actionRecipesFragmentToAuthenticationFragment())
    }

    private fun setupRecipeAdapter() {
        Timber.v("setupRecipeAdapter() called")
        binding.recipes.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecipesPagingAdapter(viewModel) { navigateToRecipeInfo(it) }
        binding.recipes.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.recipeFlow.collect {
                Timber.d("Received update")
                adapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            adapter.listenToRefreshRequests(binding.refresher)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            adapter.onPagesUpdatedFlow.collect {
                Timber.d("Pages have been updated")
                binding.refresher.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        _binding = null
    }
}