package gq.kirmanak.mealient.ui.images

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.BuildConfig
import gq.kirmanak.mealient.di.AUTH_OK_HTTP
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PicassoBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named(AUTH_OK_HTTP) private val okHttpClient: OkHttpClient
) {

    fun buildPicasso(): Picasso {
        Timber.v("buildPicasso() called")
        val builder = Picasso.Builder(context)
        builder.downloader(OkHttp3Downloader(okHttpClient))
        if (BuildConfig.DEBUG_PICASSO) {
            builder.loggingEnabled(true)
            builder.indicatorsEnabled(true)
            builder.listener { _, uri, exception ->
                Timber.tag("Picasso").e(exception, "Can't load from $uri")
            }
        }
        return builder.build()
    }
}