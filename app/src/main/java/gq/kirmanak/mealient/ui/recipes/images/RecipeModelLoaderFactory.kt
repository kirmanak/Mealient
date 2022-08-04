package gq.kirmanak.mealient.ui.recipes.images

import com.bumptech.glide.load.model.*
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeModelLoaderFactory @Inject constructor(
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
) : ModelLoaderFactory<RecipeSummaryEntity, InputStream> {

    private val cache = ModelCache<RecipeSummaryEntity, GlideUrl>()

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<RecipeSummaryEntity, InputStream> {
        Timber.v("build() called with: multiFactory = $multiFactory")
        val concreteLoader = multiFactory.build(GlideUrl::class.java, InputStream::class.java)
        return RecipeModelLoader(recipeImageUrlProvider, concreteLoader, cache)
    }

    override fun teardown() {
        // Do nothing
    }
}