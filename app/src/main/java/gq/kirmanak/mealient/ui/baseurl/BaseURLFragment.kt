package gq.kirmanak.mealient.ui.baseurl

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentBaseUrlBinding
import gq.kirmanak.mealient.datasource.CertificateCombinedException
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.extensions.collectWhenResumed
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.OperationUiState
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import gq.kirmanak.mealient.ui.baseurl.BaseURLFragmentDirections.Companion.actionBaseURLFragmentToRecipesListFragment
import java.security.cert.X509Certificate
import javax.inject.Inject

@AndroidEntryPoint
class BaseURLFragment : Fragment(R.layout.fragment_base_url) {

    private val binding by viewBinding(FragmentBaseUrlBinding::bind)
    private val viewModel by viewModels<BaseURLViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val args by navArgs<BaseURLFragmentArgs>()

    @Inject
    lateinit var logger: Logger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        binding.button.setOnClickListener(::onProceedClick)
        viewModel.uiState.observe(viewLifecycleOwner, ::onUiStateChange)
        collectWhenResumed(viewModel.invalidCertificatesFlow, ::onInvalidCertificate)
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = !args.isOnboarding,
                searchVisible = false,
                checkedMenuItem = CheckableMenuItem.ChangeUrl,
            )
        }
    }

    private fun onInvalidCertificate(certificate: X509Certificate) {
        logger.v { "onInvalidCertificate() called with: certificate = $certificate" }
        val dialogMessage = getString(
            R.string.fragment_base_url_invalid_certificate_message,
            certificate.issuerDN.toString(),
            certificate.subjectDN.toString(),
            certificate.notBefore.toString(),
            certificate.notAfter.toString(),
        )
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.fragment_base_url_invalid_certificate_title)
            .setMessage(dialogMessage)
            .setPositiveButton(R.string.fragment_base_url_invalid_certificate_accept) { _, _ ->
                viewModel.acceptInvalidCertificate(certificate)
                saveEnteredUrl()
            }.setNegativeButton(R.string.fragment_base_url_invalid_certificate_deny) { _, _ ->
                // Do nothing, let the user enter another address or try again
            }
            .create()
        dialog.show()
    }

    private fun onProceedClick(view: View) {
        logger.v { "onProceedClick() called with: view = $view" }
        saveEnteredUrl()
    }

    private fun saveEnteredUrl() {
        logger.v { "saveEnteredUrl() called" }
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

            is CertificateCombinedException -> getString(R.string.fragment_base_url_invalid_certificate_title)
            null -> null
            else -> getString(R.string.fragment_base_url_unknown_error)
        }

        uiState.updateButtonState(button)
        uiState.updateProgressState(progress)
    }
}