package gq.kirmanak.mealie.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealie.databinding.FragmentRecipesBinding
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding: FragmentRecipesBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val viewModel by viewModels<RecipeViewModel>()

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
        binding.recipes.layoutManager = LinearLayoutManager(requireContext())
        val recipesPagingAdapter = RecipesPagingAdapter(viewModel)
        binding.recipes.adapter = recipesPagingAdapter
        lifecycleScope.launchWhenResumed {
            Timber.d("onViewCreated: coroutine started")
            viewModel.recipeFlow.collectLatest {
                Timber.d("onViewCreated: received $it")
                recipesPagingAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        _binding = null
    }
}