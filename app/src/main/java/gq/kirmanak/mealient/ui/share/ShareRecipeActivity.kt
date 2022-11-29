package gq.kirmanak.mealient.ui.share

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.postDelayed
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.databinding.ActivityShareRecipeBinding
import gq.kirmanak.mealient.extensions.showLongToast
import gq.kirmanak.mealient.ui.BaseActivity
import gq.kirmanak.mealient.ui.OperationUiState

@AndroidEntryPoint
class ShareRecipeActivity : BaseActivity<ActivityShareRecipeBinding>(
    binder = ActivityShareRecipeBinding::bind,
    containerId = R.id.root,
    layoutRes = R.layout.activity_share_recipe,
) {

    private val viewModel: ShareRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        restartAnimationOnEnd()
        viewModel.saveResult.observe(this, ::onStateUpdate)
        viewModel.saveRecipeByURL(url)
    }

    private fun onStateUpdate(state: OperationUiState<String>) {
        binding.progress.isInvisible = !state.isProgress
        withAnimatedDrawable {
            if (state.isProgress) start() else stop()
        }
        if (state.isSuccess || state.isFailure) {
            showLongToast(
                if (state.isSuccess) R.string.activity_share_recipe_success_toast
                else R.string.activity_share_recipe_failure_toast
            )
            finish()
        }
    }

    private fun restartAnimationOnEnd() {
        withAnimatedDrawable {
            onAnimationEnd {
                if (viewModel.saveResult.value?.isProgress == true) {
                    binding.progress.postDelayed(250) { start() }
                }
            }
        }
    }

    private inline fun withAnimatedDrawable(block: AnimatedVectorDrawable.() -> Unit) {
        binding.progress.drawable.let { drawable ->
            if (drawable is AnimatedVectorDrawable) {
                drawable.block()
            } else {
                logger.w { "withAnimatedDrawable: progress's drawable is not AnimatedVectorDrawable" }
            }
        }
    }
}

private inline fun AnimatedVectorDrawable.onAnimationEnd(
    crossinline block: AnimatedVectorDrawable.() -> Unit,
): Animatable2.AnimationCallback {
    val callback = object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            block()
        }
    }
    registerAnimationCallback(callback)
    return callback
}