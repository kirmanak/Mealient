package gq.kirmanak.mealient.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.databinding.FragmentAuthenticationBinding
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        listenToAuthenticationStatuses()
    }

    private fun listenToAuthenticationStatuses() {
        Timber.d("listenToAuthenticationStatuses() called")
        lifecycleScope.launchWhenCreated {
            viewModel.authenticationStatuses().collectLatest {
                Timber.d("listenToAuthenticationStatuses: new status = $it")
                if (it) navigateToRecipes()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.button.setOnClickListener { onLoginClicked() }
    }

    private fun navigateToRecipes() {
        Timber.v("navigateToRecipes() called")
        findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToRecipesFragment())
    }

    private fun onLoginClicked() {
        Timber.v("onLoginClicked() called")
        val email: String
        val pass: String
        val url: String
        with(binding) {
            email = checkIfInputIsEmpty(emailInput, emailInputLayout) {
                "Email is empty"
            } ?: return
            pass = checkIfInputIsEmpty(passwordInput, passwordInputLayout) {
                "Pass is empty"
            } ?: return
            url = checkIfInputIsEmpty(urlInput, urlInputLayout) {
                "URL is empty"
            } ?: return
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            runCatching {
                viewModel.authenticate(email, pass, url)
            }.onFailure {
                Timber.e(it, "Can't authenticate")
            }
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
        if (text.isNullOrBlank()) {
            inputLayout.error = errorText()
            return null
        }
        return text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        _binding = null
    }
}