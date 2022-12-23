package gq.kirmanak.mealient

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
        isDynamicColor && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        isDynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        isDarkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

object Dimensions {

    val Small = 8.dp

    val Medium = 16.dp

    val Large = 24.dp
}