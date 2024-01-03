package gq.kirmanak.mealient.ui.activity

import android.app.Application
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.logging.getLogFile
import gq.kirmanak.mealient.ui.destinations.AuthenticationScreenDestination
import gq.kirmanak.mealient.ui.destinations.BaseURLScreenDestination
import gq.kirmanak.mealient.ui.destinations.DirectionDestination
import gq.kirmanak.mealient.ui.destinations.DisclaimerScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    private val application: Application,
    private val authRepo: AuthRepo,
    private val logger: Logger,
    private val disclaimerStorage: DisclaimerStorage,
    private val serverInfoRepo: ServerInfoRepo,
) : ViewModel() {

    private val _appState = MutableStateFlow(MealientAppState())
    val appState: StateFlow<MealientAppState> get() = _appState.asStateFlow()

    init {
        checkForcedDestination()
    }

    private fun checkForcedDestination() {
        logger.v { "checkForcedDestination() called" }
        val baseUrlSetState = serverInfoRepo.baseUrlFlow.map { it != null }
        val tokenExistsState = authRepo.isAuthorizedFlow
        val disclaimerAcceptedState = disclaimerStorage.isDisclaimerAcceptedFlow

        viewModelScope.launch {
            combine(
                baseUrlSetState,
                tokenExistsState,
                disclaimerAcceptedState,
            ) { baseUrlSet, tokenExists, disclaimerAccepted ->
                logger.d { "baseUrlSet = $baseUrlSet, tokenExists = $tokenExists, disclaimerAccepted = $disclaimerAccepted" }
                when {
                    !disclaimerAccepted -> ForcedDestination.Destination(DisclaimerScreenDestination)
                    !baseUrlSet -> ForcedDestination.Destination(BaseURLScreenDestination)
                    !tokenExists -> ForcedDestination.Destination(AuthenticationScreenDestination)
                    else -> ForcedDestination.None
                }
            }.collect { destination ->
                logger.v { "destination = $destination" }
                _appState.update { it.copy(forcedRoute = destination) }
            }
        }
    }

    fun onEvent(event: AppEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is AppEvent.Logout -> {
                _appState.update {
                    it.copy(dialogState = logoutConfirmationDialog())
                }
            }

            is AppEvent.LogoutConfirm -> {
                _appState.update { it.copy(dialogState = null) }
                viewModelScope.launch { authRepo.logout() }
            }

            is AppEvent.EmailLogs -> {
                _appState.update {
                    it.copy(dialogState = emailConfirmationDialog())
                }
            }

            is AppEvent.EmailLogsConfirm -> {
                _appState.update {
                    it.copy(
                        dialogState = null,
                        intentToLaunch = logEmailIntent(),
                    )
                }
            }

            is AppEvent.DismissDialog -> {
                _appState.update { it.copy(dialogState = null) }
            }

            is AppEvent.LaunchedIntent -> {
                _appState.update { it.copy(intentToLaunch = null) }
            }
        }
    }

    private fun logEmailIntent(): Intent? {
        val logFileUri = try {
            FileProvider.getUriForFile(
                /* context = */ application,
                /* authority = */ "${application.packageName}.provider",
                /* file = */ application.getLogFile(),
            )
        } catch (e: IllegalArgumentException) {
            logger.e(e) { "Failed to get log file URI" }
            return null
        }

        if (logFileUri == null) {
            logger.e { "logFileUri is null" }
            return null
        }

        logger.v { "logFileUri = $logFileUri" }

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            val subject = application.getString(R.string.activity_main_email_logs_subject)
            val to = arrayOf(EMAIL_FOR_LOGS)
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, to)
            putExtra(Intent.EXTRA_STREAM, logFileUri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        return Intent.createChooser(emailIntent, null)
    }

    private fun logoutConfirmationDialog() = DialogState(
        title = R.string.activity_main_logout_confirmation_title,
        message = R.string.activity_main_logout_confirmation_message,
        positiveButton = R.string.activity_main_logout_confirmation_positive,
        negativeButton = R.string.activity_main_logout_confirmation_negative,
        onPositiveClick = AppEvent.LogoutConfirm,
    )

    private fun emailConfirmationDialog() = DialogState(
        title = R.string.activity_main_email_logs_confirmation_title,
        message = R.string.activity_main_email_logs_confirmation_message,
        positiveButton = R.string.activity_main_email_logs_confirmation_positive,
        negativeButton = R.string.activity_main_email_logs_confirmation_negative,
        onPositiveClick = AppEvent.EmailLogsConfirm,
    )

    companion object {
        private const val EMAIL_FOR_LOGS = "mealient@gmail.com"
    }
}

internal data class MealientAppState(
    val forcedRoute: ForcedDestination = ForcedDestination.Undefined,
    val searchQuery: String = "",
    val dialogState: DialogState? = null,
    val intentToLaunch: Intent? = null,
)

internal data class DialogState(
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positiveButton: Int,
    @StringRes val negativeButton: Int,
    val onPositiveClick: AppEvent,
    val onDismiss: AppEvent = AppEvent.DismissDialog,
    val onNegativeClick: AppEvent = onDismiss,
)

internal sealed interface ForcedDestination {

    /**
     * Force navigation is required
     */
    data class Destination(
        val route: DirectionDestination,
    ) : ForcedDestination

    /**
     * The conditions were checked, no force navigation required
     */
    data object None : ForcedDestination

    /**
     * The conditions were not checked yet
     */
    data object Undefined : ForcedDestination
}

internal sealed interface AppEvent {

    data object Logout : AppEvent

    data object EmailLogs : AppEvent

    data object DismissDialog : AppEvent

    data object LogoutConfirm : AppEvent

    data object EmailLogsConfirm : AppEvent

    data object LaunchedIntent : AppEvent
}