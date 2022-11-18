package gq.kirmanak.mealient.ui.baseurl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentBaseUrlBinding
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.OperationUiState
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import gq.kirmanak.mealient.ui.baseurl.BaseURLFragmentDirections.Companion.actionBaseURLFragmentToRecipesListFragment
import javax.inject.Inject

@AndroidEntryPoint
class BaseURLFragment : Fragment(R.layout.fragment_base_url) {

    private val binding by viewBinding(FragmentBaseUrlBinding::bind)
    private val viewModel by viewModels<BaseURLViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        binding.button.setOnClickListener(::onProceedClick)
        viewModel.uiState.observe(viewLifecycleOwner, ::onUiStateChange)
        activityViewModel.updateUiState {
            it.copy(loginButtonVisible = false, navigationVisible = false, searchVisible = false)
        }
    }

    private fun onProceedClick(view: View) {
        logger.v { "onProceedClick() called with: view = $view" }
        val url = binding.urlInput.checkIfInputIsEmpty(
            inputLayout = binding.urlInputLayout,
            lifecycleOwner = viewLifecycleOwner,
            stringId = R.string.fragment_baseurl_url_input_empty,
            logger = logger,
        ) ?: return
        viewModel.saveBaseUrl(url)
    }

    private fun onUiStateChange(uiState: OperationUiState<Unit>) = with(binding) {
        logger.v { "onUiStateChange() called with: uiState = $uiState" }
        if (uiState.isSuccess) {
            findNavController().navigate(actionBaseURLFragmentToRecipesListFragment())
            return
        }
        urlInputLayout.error = when (val exception = uiState.exceptionOrNull) {
            is NetworkError.NoServerConnection -> getString(R.string.fragment_base_url_no_connection)
            is NetworkError.NotMealie -> getString(R.string.fragment_base_url_unexpected_response)
            is NetworkError.MalformedUrl -> {
                val cause = exception.cause?.message ?: exception.message
                getString(R.string.fragment_base_url_malformed_url, cause)
            }
            null -> null
            else -> getString(R.string.fragment_base_url_unknown_error)
        }

        uiState.updateButtonState(button)
        uiState.updateProgressState(progress)
    }
}