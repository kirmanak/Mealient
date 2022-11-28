package gq.kirmanak.mealient.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.extensions.showLongToast
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@AndroidEntryPoint
class ShareRecipeActivity : AppCompatActivity() {

    private val viewModel: ShareRecipeViewModel by viewModels()

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }

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

        viewModel.saveOperationResult.observe(this) {
            showLongToast(
                if (it.isSuccess) R.string.activity_share_recipe_success_toast
                else R.string.activity_share_recipe_failure_toast
            )
            finish()
        }

        viewModel.saveRecipeByURL(url)
    }
}