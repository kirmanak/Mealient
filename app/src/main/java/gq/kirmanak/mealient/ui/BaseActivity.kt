package gq.kirmanak.mealient.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import gq.kirmanak.mealient.extensions.isDarkThemeOn
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding>(
    binder: (View) -> T,
    @IdRes containerId: Int,
    @LayoutRes layoutRes: Int,
) : AppCompatActivity(layoutRes) {

    protected val binding by viewBinding(binder, containerId)

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.v { "onCreate() called with: savedInstanceState = $savedInstanceState" }
        setContentView(binding.root)
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            val isAppearanceLightBars = !isDarkThemeOn()
            isAppearanceLightNavigationBars = isAppearanceLightBars
            isAppearanceLightStatusBars = isAppearanceLightBars
        }
    }
}