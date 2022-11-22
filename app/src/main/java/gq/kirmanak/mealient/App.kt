package gq.kirmanak.mealient

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var buildConfiguration: BuildConfiguration

    override fun onCreate() {
        super.onCreate()
        logger.v { "onCreate() called" }
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
