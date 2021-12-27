package gq.kirmanak.mealient

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
  // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
  @Inject
  lateinit var flipperPlugins: Set<@JvmSuppressWildcards FlipperPlugin>

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Timber.v("onCreate() called")
    setupFlipper()
  }

  private fun setupFlipper() {
    if (FlipperUtils.shouldEnableFlipper(this)) {
      SoLoader.init(this, false)
      val flipperClient = AndroidFlipperClient.getInstance(this)
      for (flipperPlugin in flipperPlugins) flipperClient.addPlugin(flipperPlugin)
      flipperClient.start()
    }
  }
}
