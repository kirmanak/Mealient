package gq.kirmanak.mealient.ui.disclaimer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.FragmentDisclaimerBinding
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DisclaimerFragment : Fragment(R.layout.fragment_disclaimer) {

    private val binding by viewBinding(FragmentDisclaimerBinding::bind)
    private val viewModel by viewModels<DisclaimerViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }
        viewModel.isAccepted.observe(this, ::onAcceptStateChange)
    }

    private fun onAcceptStateChange(isAccepted: Boolean) {
        logger.v { "onAcceptStateChange() called with: isAccepted = $isAccepted" }
        if (isAccepted) navigateNext()
    }

    private fun navigateNext() {
        logger.v { "navigateNext() called" }
        findNavController().navigate(DisclaimerFragmentDirections.actionDisclaimerFragmentToBaseURLFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        binding.okay.setOnClickListener {
            logger.v { "onViewCreated: okay clicked" }
            viewModel.acceptDisclaimer()
        }
        viewModel.okayCountDown.observe(viewLifecycleOwner) {
            logger.d { "onViewCreated: new count $it" }
            binding.okay.text = if (it > 0) resources.getQuantityString(
                R.plurals.fragment_disclaimer_button_okay_timer, it, it
            ) else getString(R.string.fragment_disclaimer_button_okay)
            binding.okay.isClickable = it == 0
        }
        viewModel.startCountDown()
        activityViewModel.updateUiState {
            it.copy(loginButtonVisible = false, titleVisible = true, navigationVisible = false)
        }
    }
}