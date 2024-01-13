package gq.kirmanak.mealient.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.extensions.isDarkThemeOn
import gq.kirmanak.mealient.extensions.showLongToast
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.OperationUiState
import javax.inject.Inject

@AndroidEntryPoint
class ShareRecipeActivity : ComponentActivity() {

    @Inject
    lateinit var logger: Logger

    private val viewModel: ShareRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            val isAppearanceLightBars = !isDarkThemeOn()
            isAppearanceLightNavigationBars = isAppearanceLightBars
            isAppearanceLightStatusBars = isAppearanceLightBars
        }

        if (intent.action != Intent.ACTION_SEND || intent.type != "text/plain") {
            logger.w { "onCreate: intent.action = ${intent.action}, intent.type = ${intent.type}" }
            finish()
            return
        }

        val url: CharSequence? = intent.getCharSequenceExtra(Intent.EXTRA_TEXT)
        if (url == null) {
            logger.w { "onCreate: Intent's EXTRA_TEXT was null" }
            finish()
            return
        }

        viewModel.saveResult.observe(this, ::onStateUpdate)
        viewModel.saveRecipeByURL(url)

        setContent {
            AppTheme {
                ShareRecipeScreen()
            }
        }
    }

    private fun onStateUpdate(state: OperationUiState<String>) {
        if (state.isSuccess || state.isFailure) {
            showLongToast(
                if (state.isSuccess) R.string.activity_share_recipe_success_toast
                else R.string.activity_share_recipe_failure_toast
            )
            finish()
        }
    }

}

