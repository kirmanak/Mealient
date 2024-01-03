package gq.kirmanak.mealient.ui.share

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.components.BaseScreen
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import kotlinx.coroutines.delay

@Composable
internal fun ShareRecipeScreen() {
    BaseScreen { modifier ->
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = loopAnimationPainter(
                    resId = R.drawable.ic_progress_bar,
                    delayBeforeRestart = 1000, // Animation takes 800 ms
                ),
                contentDescription = stringResource(
                    id = R.string.content_description_activity_share_recipe_progress,
                ),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Suppress("SameParameterValue")
private fun loopAnimationPainter(
    @DrawableRes resId: Int,
    delayBeforeRestart: Long,
): Painter {
    val image = AnimatedImageVector.animatedVectorResource(id = resId)

    var atEnd by remember { mutableStateOf(false) }
    LaunchedEffect(image) {
        while (true) {
            delay(delayBeforeRestart)
            atEnd = !atEnd
        }
    }

    return rememberAnimatedVectorPainter(
        animatedImageVector = image,
        atEnd = atEnd,
    )
}

@ColorSchemePreview
@Composable
private fun ShareRecipeScreenPreview() {
    AppTheme {
        ShareRecipeScreen()
    }
}