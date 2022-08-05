package gq.kirmanak.mealient.ui

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.android.EntryPointAccessors.fromApplication
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.di.GlideModuleEntryPoint
import gq.kirmanak.mealient.logging.Logger
import java.io.InputStream

@GlideModule
class MealieGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        getLogger(context).v { "registerComponents() called with: context = $context, glide = $glide, registry = $registry" }
        replaceOkHttp(context, registry)
        appendRecipeLoader(registry, context)
    }

    private fun appendRecipeLoader(registry: Registry, context: Context) {
        getLogger(context).v { "appendRecipeLoader() called with: registry = $registry, context = $context" }
        registry.append(
            RecipeSummaryEntity::class.java,
            InputStream::class.java,
            getEntryPoint(context).provideRecipeLoaderFactory(),
        )
    }

    private fun replaceOkHttp(context: Context, registry: Registry) {
        getLogger(context).v { "replaceOkHttp() called with: context = $context, registry = $registry" }
        val okHttp = getEntryPoint(context).provideOkHttp()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttp)
        )
    }

    private fun getEntryPoint(context: Context): GlideModuleEntryPoint =
        fromApplication(context, GlideModuleEntryPoint::class.java)

    private fun getLogger(context: Context): Logger = getEntryPoint(context).provideLogger()
}
