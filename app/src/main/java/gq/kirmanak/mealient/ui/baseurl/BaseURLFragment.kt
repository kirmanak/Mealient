package gq.kirmanak.mealient.ui.baseurl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.databinding.FragmentBaseUrlBinding
import timber.log.Timber

@AndroidEntryPoint
class BaseURLFragment : Fragment(R.layout.fragment_base_url) {

    private val binding by viewBinding(FragmentBaseUrlBinding::bind)
    private val viewModel by viewModels<BaseURLViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        viewModel.screenState.observe(viewLifecycleOwner, ::updateState)
        binding.button.setOnClickListener {
            viewModel.saveBaseUrl(binding.urlInput.text.toString())
        }
    }

    private fun updateState(baseURLScreenState: BaseURLScreenState) {
        Timber.v("updateState() called with: baseURLScreenState = $baseURLScreenState")
        if (baseURLScreenState.navigateNext) {
            findNavController().navigate(BaseURLFragmentDirections.actionBaseURLFragmentToRecipesFragment())
            return
        }
        binding.urlInputLayout.error = when (val exception = baseURLScreenState.error) {
            is NetworkError.NoServerConnection -> getString(R.string.fragment_base_url_no_connection)
            is NetworkError.NotMealie -> getString(R.string.fragment_base_url_unexpected_response)
            is NetworkError.MalformedUrl -> {
                val cause = exception.cause?.message ?: exception.message
                getString(R.string.fragment_base_url_malformed_url, cause)
            }
            null -> null
            else -> getString(R.string.fragment_base_url_unknown_error)
        }
    }
}