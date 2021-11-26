package gq.kirmanak.mealient.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesBinding
import gq.kirmanak.mealient.ui.auth.AuthenticationViewModel
import gq.kirmanak.mealient.ui.refreshesLiveData
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
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        setupRecipeAdapter()
        authStatuses.observe(this, authStatusObserver)
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

    private fun navigateToAuthFragment() {
        Timber.v("navigateToAuthFragment() called")
        findNavController().navigate(RecipesFragmentDirections.actionRecipesFragmentToAuthenticationFragment())
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
        _binding = null
    }
}