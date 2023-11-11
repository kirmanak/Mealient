package gq.kirmanak.mealient.ui.recipes.info

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import gq.kirmanak.mealient.extensions.findActivity

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    val window = context.findActivity()?.window ?: return
    DisposableEffect(Unit) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}