package gq.kirmanak.mealient

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
        logger.v { "onCreate() called" }
    }
}
