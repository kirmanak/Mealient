package gq.kirmanak.mealient.ui.disclaimer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentDisclaimerBinding
import timber.log.Timber

@AndroidEntryPoint
class DisclaimerFragment : Fragment(R.layout.fragment_disclaimer) {
    private val binding by viewBinding(FragmentDisclaimerBinding::bind)
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
            if (it) navigateNext()
        }
        viewModel.checkIsAccepted()
    }

    private fun navigateNext() {
        Timber.v("navigateNext() called")
        findNavController().navigate(DisclaimerFragmentDirections.actionDisclaimerFragmentToBaseURLFragment())
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
            binding.okay.text = if (it > 0) resources.getQuantityString(
                R.plurals.fragment_disclaimer_button_okay_timer, it, it
            ) else getString(R.string.fragment_disclaimer_button_okay)
            binding.okay.isClickable = it == 0
        }
        viewModel.startCountDown()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.app_name)
    }
}