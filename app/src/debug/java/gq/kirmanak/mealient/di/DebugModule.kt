package gq.kirmanak.mealient.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.BuildConfig
import gq.kirmanak.mealient.logging.Logger
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideLoggingInterceptor(logger: Logger): Interceptor {
        val interceptor = HttpLoggingInterceptor { message -> logger.v(tag = "OkHttp") { message } }
        interceptor.level = when {
            BuildConfig.LOG_NETWORK -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.BASIC
        }
        return interceptor
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideChuckerInterceptor(@ApplicationContext context: Context): Interceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR,
        )
        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            .alwaysReadResponseBody(true)
            .build()
    }
}
