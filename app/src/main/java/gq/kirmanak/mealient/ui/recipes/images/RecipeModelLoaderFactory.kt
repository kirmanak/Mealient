package gq.kirmanak.mealient.ui.recipes.images

import com.bumptech.glide.load.model.*
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeModelLoaderFactory @Inject constructor(
    private val recipeModelLoaderFactory: RecipeModelLoader.Factory,
    private val logger: Logger,
) : ModelLoaderFactory<RecipeSummaryEntity, InputStream> {

    private val cache = ModelCache<RecipeSummaryEntity, GlideUrl>()

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<RecipeSummaryEntity, InputStream> {
        logger.v { "build() called with: multiFactory = $multiFactory" }
        val concreteLoader = multiFactory.build(GlideUrl::class.java, InputStream::class.java)
        return recipeModelLoaderFactory.build(concreteLoader, cache)
    }

    override fun teardown() {
        // Do nothing
    }
}