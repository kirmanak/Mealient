package gq.kirmanak.mealient.ui.disclaimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentDisclaimerBinding
import timber.log.Timber

@AndroidEntryPoint
class DisclaimerFragment : Fragment() {
    private var _binding: FragmentDisclaimerBinding? = null
    private val binding: FragmentDisclaimerBinding
        get() = checkNotNull(_binding) { "Binding requested when fragment is off screen" }
    private val viewModel by viewModels<DisclaimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        listenToAcceptStatus()
    }

    private fun listenToAcceptStatus() {
        Timber.v("listenToAcceptStatus() called")
        viewModel.isAccepted.observe(this) {
            Timber.d("listenToAcceptStatus: new status = $it")
            if (it) navigateToAuth()
        }
        viewModel.checkIsAccepted()
    }

    private fun navigateToAuth() {
        Timber.v("navigateToAuth() called")
        findNavController().navigate(DisclaimerFragmentDirections.actionDisclaimerFragmentToAuthenticationFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        _binding = FragmentDisclaimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        binding.okay.setOnClickListener {
            Timber.v("onViewCreated: okay clicked")
            viewModel.acceptDisclaimer()
        }
        viewModel.okayCountDown.observe(viewLifecycleOwner) {
            Timber.d("onViewCreated: new count $it")
            binding.okay.text = if (it > 0) {
                getString(R.string.fragment_disclaimer_button_okay_timer, it)
            } else {
                getString(R.string.fragment_disclaimer_button_okay)
            }
            binding.okay.isClickable = it == 0
        }
        viewModel.startCountDown()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.app_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView() called")
        _binding = null
    }
}