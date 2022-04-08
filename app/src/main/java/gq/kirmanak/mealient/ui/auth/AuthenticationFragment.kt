package gq.kirmanak.mealient.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private val binding by viewBinding(FragmentAuthenticationBinding::bind)
    private val viewModel by viewModels<AuthenticationViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
        activityViewModel.updateUiState { it.copy(loginButtonVisible = false, titleVisible = true) }
        viewModel.authenticationResult.observe(viewLifecycleOwner, ::onAuthenticationResult)
    }

    private fun onLoginClicked(): Unit = with(binding) {
        Timber.v("onLoginClicked() called")

        val email: String = emailInput.checkIfInputIsEmpty(
            inputLayout = emailInputLayout,
            lifecycleOwner = viewLifecycleOwner,
            stringId = R.string.fragment_authentication_email_input_empty,
        ) ?: return

        val pass: String = passwordInput.checkIfInputIsEmpty(
            inputLayout = passwordInputLayout,
            lifecycleOwner = viewLifecycleOwner,
            stringId = R.string.fragment_authentication_password_input_empty,
            trim = false,
        ) ?: return

        button.isClickable = false
        viewModel.authenticate(email, pass)
    }

    private fun onAuthenticationResult(result: Result<Unit>) {
        Timber.v("onAuthenticationResult() called with: result = $result")
        if (result.isSuccess) {
            findNavController().popBackStack()
            return
        }

        binding.passwordInputLayout.error = when (result.exceptionOrNull()) {
            is NetworkError.Unauthorized -> getString(R.string.fragment_authentication_credentials_incorrect)
            else -> null
        }

        binding.button.isClickable = true
    }
}