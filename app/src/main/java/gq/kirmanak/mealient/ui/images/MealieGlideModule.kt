package gq.kirmanak.mealient.ui.images

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.android.EntryPointAccessors.fromApplication
import timber.log.Timber
import java.io.InputStream

@GlideModule
class MealieGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        replaceOkHttp(context, registry)
    }

    private fun replaceOkHttp(context: Context, registry: Registry) {
        Timber.v("replaceOkHttp() called with: context = $context, registry = $registry")
        val entryPoint = fromApplication(context, GlideModuleEntryPoint::class.java)
        val okHttp = entryPoint.provideOkHttp()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttp)
        )
    }
}
