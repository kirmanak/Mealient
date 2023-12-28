package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.extensions.isDarkThemeOn
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

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

}