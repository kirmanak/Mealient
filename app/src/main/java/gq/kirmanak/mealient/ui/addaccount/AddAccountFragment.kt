package gq.kirmanak.mealient.ui.addaccount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import gq.kirmanak.mealient.extensions.checkIfInputIsEmpty
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddAccountFragment : Fragment(R.layout.fragment_authentication) {

    private val binding by viewBinding(FragmentAuthenticationBinding::bind)
    private val viewModel by activityViewModels<AddAccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
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
        viewLifecycleOwner.lifecycleScope.launch {
            onAuthenticationResult(viewModel.authenticate(email, pass))
        }
    }

    private fun onAuthenticationResult(result: Result<Unit>) {
        Timber.v("onAuthenticationResult() called with: result = $result")
        if (result.isSuccess) {
            TODO("Implement authentication success")
        }

        binding.passwordInputLayout.error = when (result.exceptionOrNull()) {
            is NetworkError.Unauthorized -> getString(R.string.fragment_authentication_credentials_incorrect)
            else -> null
        }

        binding.button.isClickable = true
    }
}