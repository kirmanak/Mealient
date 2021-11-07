package gq.kirmanak.mealie.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealie.databinding.FragmentAuthenticationBinding

private const val TAG = "AuthenticationFragment"

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAuthenticatedAlready()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            onLoginClicked()
        }
    }

    private fun checkIfAuthenticatedAlready() {
        lifecycleScope.launchWhenCreated {
            Toast.makeText(
                requireContext(),
                if (viewModel.isAuthenticated()) "User is authenticated"
                else "User isn't authenticated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onLoginClicked() {
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
        lifecycleScope.launchWhenResumed {
            val exception = viewModel.authenticate(email, pass, url)
            Log.e(TAG, "onLoginClicked: ", exception)
            Toast.makeText(
                requireContext(),
                "Exception is ${exception?.message ?: "null"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkIfInputIsEmpty(
        input: EditText,
        inputLayout: TextInputLayout,
        errorText: () -> String
    ): String? {
        val text = input.text?.toString()
        if (text.isNullOrBlank()) {
            inputLayout.error = errorText()
            return null
        }
        return text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}