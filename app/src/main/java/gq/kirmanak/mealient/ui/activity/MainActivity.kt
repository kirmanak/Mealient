package gq.kirmanak.mealient.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.extensions.isDarkThemeOn
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.logging.getLogFile
import javax.inject.Inject

private const val EMAIL_FOR_LOGS = "mealient@gmail.com"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var logger: Logger

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            val isAppearanceLightBars = !isDarkThemeOn()
            isAppearanceLightNavigationBars = isAppearanceLightBars
            isAppearanceLightStatusBars = isAppearanceLightBars
        }
        splashScreen.setKeepOnScreenCondition {
            viewModel.appState.value.forcedRoute == ForcedDestination.Undefined
        }
        setContent {
            MealientApp(viewModel)
        }
    }
    private fun emailLogs() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.activity_main_email_logs_confirmation_message)
            .setTitle(R.string.activity_main_email_logs_confirmation_title)
            .setPositiveButton(R.string.activity_main_email_logs_confirmation_positive) { _, _ -> doEmailLogs() }
            .setNegativeButton(R.string.activity_main_email_logs_confirmation_negative, null)
            .show()
    }

    private fun doEmailLogs() {
        val logFileUri = try {
            FileProvider.getUriForFile(this, "$packageName.provider", getLogFile())
        } catch (e: Exception) {
            return
        }
        val emailIntent = buildIntent(logFileUri)
        val chooserIntent = Intent.createChooser(emailIntent, null)
        startActivity(chooserIntent)
    }

    private fun buildIntent(logFileUri: Uri?): Intent {
        val emailIntent = Intent(Intent.ACTION_SEND)
        val to = arrayOf(EMAIL_FOR_LOGS)
        emailIntent.setType("text/plain")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        emailIntent.putExtra(Intent.EXTRA_STREAM, logFileUri)
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.activity_main_email_logs_subject)
        )
        return emailIntent
    }
}