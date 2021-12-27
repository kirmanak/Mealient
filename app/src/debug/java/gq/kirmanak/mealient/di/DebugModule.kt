package gq.kirmanak.mealient.di

import android.content.Context
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.FlipperLeakListener
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import leakcanary.LeakCanary
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugModule {
  @Provides
  @Singleton
  @IntoSet
  fun provideLoggingInterceptor(): Interceptor {
    val interceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").v(message) }
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
  }

  @Provides
  @Singleton
  @IntoSet
  fun provideFlipperInterceptor(networkFlipperPlugin: NetworkFlipperPlugin): Interceptor {
    return FlipperOkhttpInterceptor(networkFlipperPlugin)
  }

  @Provides
  @Singleton
  fun networkFlipperPlugin() = NetworkFlipperPlugin()

  @Provides
  @Singleton
  @IntoSet
  fun bindNetworkFlipperPlugin(plugin: NetworkFlipperPlugin): FlipperPlugin = plugin

  @Provides
  @Singleton
  @IntoSet
  fun sharedPreferencesPlugin(@ApplicationContext context: Context): FlipperPlugin =
    SharedPreferencesFlipperPlugin(context)

  @Provides
  @Singleton
  @IntoSet
  fun leakCanaryPlugin(): FlipperPlugin {
    LeakCanary.config = LeakCanary.config.copy(onHeapAnalyzedListener = FlipperLeakListener())
    return LeakCanary2FlipperPlugin()
  }

  @Provides
  @Singleton
  @IntoSet
  fun databasesPlugin(@ApplicationContext context: Context): FlipperPlugin =
    DatabasesFlipperPlugin(context)

  @Provides
  @Singleton
  @IntoSet
  fun inspectorPlugin(@ApplicationContext context: Context): FlipperPlugin =
    InspectorFlipperPlugin(context, DescriptorMapping.withDefaults())
}
