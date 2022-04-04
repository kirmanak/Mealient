package gq.kirmanak.mealient.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.NetworkError.Unauthorized
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import gq.kirmanak.mealient.extensions.executeOnceOnBackPressed
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private val binding by viewBinding(FragmentAuthenticationBinding::bind)
    private val viewModel by activityViewModels<AuthenticationViewModel>()

    private val authStatuses: LiveData<AuthenticationState>
        get() = viewModel.authenticationState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        authStatuses.observe(this, ::onAuthStatusChange)
        executeOnceOnBackPressed { viewModel.disableLoginRequest() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.app_name)
    }

    private fun onAuthStatusChange(isAuthenticated: AuthenticationState) {
        Timber.v("onAuthStatusChange() called with: isAuthenticated = $isAuthenticated")
        if (isAuthenticated == AuthenticationState.AUTHORIZED) {
            findNavController().popBackStack()
        }
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
        viewModel.authenticate(email, pass).observe(viewLifecycleOwner) {
            Timber.d("onLoginClicked: result $it")
            passwordInputLayout.error = when (it.exceptionOrNull()) {
                is Unauthorized -> getString(R.string.fragment_authentication_credentials_incorrect)
                else -> null
            }

            button.isClickable = true
        }
    }
}