package gq.kirmanak.mealient.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.extensions.executeOnceOnBackPressed
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private val binding by viewBinding(FragmentAuthenticationBinding::bind)
    private val viewModel by activityViewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        executeOnceOnBackPressed { viewModel.authRequested = false }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.app_name)
    }

    private fun onLoginClicked(): Unit = with(binding) {
        Timber.v("onLoginClicked() called")

        val email: String = emailInput.checkIfInputIsEmpty(emailInputLayout, lifecycleScope) {
            getString(R.string.fragment_authentication_email_input_empty)
        } ?: return

        val pass: String = passwordInput.checkIfInputIsEmpty(passwordInputLayout, lifecycleScope) {
            getString(R.string.fragment_authentication_password_input_empty)
        } ?: return

        button.isClickable = false
        viewLifecycleOwner.lifecycleScope.launch {
            onAuthenticationResult(viewModel.authenticate(email, pass))
        }
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