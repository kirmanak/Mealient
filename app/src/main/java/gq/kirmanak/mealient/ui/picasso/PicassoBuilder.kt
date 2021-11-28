package gq.kirmanak.mealient.ui.picasso

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.BuildConfig
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class PicassoBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
) {

    fun buildPicasso(): Picasso {
        Timber.v("buildPicasso() called")
        val builder = Picasso.Builder(context)
        builder.downloader(OkHttp3Downloader(okHttpClient))
        if (BuildConfig.DEBUG) {
            builder.loggingEnabled(true)
            builder.indicatorsEnabled(true)
            builder.listener { _, uri, exception ->
                Timber.tag("Picasso").e(exception, "Can't load from $uri")
            }
        }
        return builder.build()
    }
}