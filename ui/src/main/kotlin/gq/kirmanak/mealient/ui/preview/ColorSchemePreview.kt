package gq.kirmanak.mealient.ui.preview

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(
    name = "Blue",
    group = "Day",
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
)
@Preview(
    name = "Red",
    group = "Day",
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
)
@Preview(
    name = "None",
    group = "Day",
    showBackground = true,
)
@Preview(
    name = "Blue",
    group = "Night",
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_MASK and UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Red",
    group = "Night",
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_MASK and UI_MODE_NIGHT_YES,
)
@Preview(
    name = "None",
    group = "Night",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_MASK and UI_MODE_NIGHT_YES,
)
annotation class ColorSchemePreview
