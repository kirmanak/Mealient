package gq.kirmanak.mealient.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.*
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import gq.kirmanak.mealient.ui.textChangesFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private val binding by viewBinding(FragmentAuthenticationBinding::bind)
    private val viewModel by viewModels<AuthenticationViewModel>()

    private val authStatuses by lazy { viewModel.authenticationStatuses() }
    private val authStatusObserver = Observer<Boolean> { onAuthStatusChange(it) }
    private fun onAuthStatusChange(isAuthenticated: Boolean) {
        Timber.v("onAuthStatusChange() called with: isAuthenticated = $isAuthenticated")
        if (isAuthenticated) {
            authStatuses.removeObserver(authStatusObserver)
            navigateToRecipes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        authStatuses.observe(this, authStatusObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.app_name)
    }

    private fun navigateToRecipes() {
        Timber.v("navigateToRecipes() called")
        findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToRecipesFragment())
    }

    private fun onLoginClicked(): Unit = with(binding) {
        Timber.v("onLoginClicked() called")

        val email: String = checkIfInputIsEmpty(emailInput, emailInputLayout) {
            getString(R.string.fragment_authentication_email_input_empty)
        } ?: return

        val pass: String = checkIfInputIsEmpty(passwordInput, passwordInputLayout) {
            getString(R.string.fragment_authentication_password_input_empty)
        } ?: return

        val url: String = checkIfInputIsEmpty(urlInput, urlInputLayout) {
            getString(R.string.fragment_authentication_url_input_empty)
        } ?: return

        button.isClickable = false
        viewModel.authenticate(email, pass, url).observe(viewLifecycleOwner) {
            Timber.d("onLoginClicked: result $it")
            passwordInputLayout.error = when (it.exceptionOrNull()) {
                is Unauthorized -> getString(R.string.fragment_authentication_credentials_incorrect)
                else -> null
            }
            urlInputLayout.error = when (val exception = it.exceptionOrNull()) {
                is NoServerConnection -> getString(R.string.fragment_authentication_no_connection)
                is NotMealie -> getString(R.string.fragment_authentication_unexpected_response)
                is MalformedUrl -> {
                    val cause = exception.cause?.message ?: exception.message
                    getString(R.string.fragment_authentication_url_invalid, cause)
                }
                is Unauthorized, null -> null
                else -> getString(R.string.fragment_authentication_unknown_error)
            }
            button.isClickable = true
        }
    }

    private fun checkIfInputIsEmpty(
        input: EditText,
        inputLayout: TextInputLayout,
        errorText: () -> String
    ): String? {
        Timber.v("checkIfInputIsEmpty() called with: input = $input, inputLayout = $inputLayout, errorText = $errorText")
        val text = input.text?.toString()
        Timber.d("Input text is \"$text\"")
        if (text.isNullOrEmpty()) {
            inputLayout.error = errorText()
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                waitUntilNotEmpty(input)
                inputLayout.error = null
            }
            return null
        }
        return text
    }

    private suspend fun waitUntilNotEmpty(input: EditText) {
        Timber.v("waitUntilNotEmpty() called with: input = $input")
        input.textChangesFlow().filterNotNull().first { it.isNotEmpty() }
        Timber.v("waitUntilNotEmpty() returned")
    }
}