package gq.kirmanak.mealient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseComposeFragment : Fragment() {

    @Inject
    lateinit var logger: Logger

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.v { "onCreateView() called" }
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Screen()
                }
            }
        }
    }

    @Composable
    abstract fun Screen()
}