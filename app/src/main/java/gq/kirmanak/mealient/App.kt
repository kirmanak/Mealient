package gq.kirmanak.mealient

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gq.kirmanak.mealient.data.analytics.Analytics
import gq.kirmanak.mealient.data.configuration.BuildConfiguration
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var buildConfiguration: BuildConfiguration

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate() {
        super.onCreate()
        logger.v { "onCreate() called" }
        analytics.setIsEnabled(!buildConfiguration.isDebug())
    }
}
