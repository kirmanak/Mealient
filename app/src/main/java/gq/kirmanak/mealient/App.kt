package gq.kirmanak.mealient

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import gq.kirmanak.mealient.data.migration.MigrationDetector
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var buildConfiguration: BuildConfiguration

    @Inject
    lateinit var migrationDetector: MigrationDetector

    @Inject
    lateinit var imageLoader: ImageLoader

    private val appCoroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate() {
        super.onCreate()
        logger.v { "onCreate() called" }
        DynamicColors.applyToActivitiesIfAvailable(this)
        appCoroutineScope.launch { migrationDetector.executeMigrations() }
        Coil.setImageLoader(imageLoader)
    }
}
