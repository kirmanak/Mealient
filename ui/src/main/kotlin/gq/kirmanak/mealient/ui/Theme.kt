package gq.kirmanak.mealient.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.material.color.DynamicColors

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = DynamicColors.isDynamicColorAvailable(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || !isDynamicColor -> {
            if (isDarkTheme) darkColorScheme() else lightColorScheme()
        }
        isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        else -> {
            dynamicLightColorScheme(LocalContext.current)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

object Dimens {

    val Small = 8.dp

    val Intermediate = 12.dp

    val Medium = 16.dp

    val Large = 24.dp
}