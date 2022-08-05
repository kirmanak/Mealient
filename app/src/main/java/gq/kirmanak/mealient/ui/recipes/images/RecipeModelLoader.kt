package gq.kirmanak.mealient.ui.recipes.images

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

class RecipeModelLoader private constructor(
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    private val logger: Logger,
    concreteLoader: ModelLoader<GlideUrl, InputStream>,
    cache: ModelCache<RecipeSummaryEntity, GlideUrl>,
) : BaseGlideUrlLoader<RecipeSummaryEntity>(concreteLoader, cache) {

    @Singleton
    class Factory @Inject constructor(
        private val recipeImageUrlProvider: RecipeImageUrlProvider,
        private val logger: Logger,
    ) {

        fun build(
            concreteLoader: ModelLoader<GlideUrl, InputStream>,
            cache: ModelCache<RecipeSummaryEntity, GlideUrl>,
        ) = RecipeModelLoader(recipeImageUrlProvider, logger, concreteLoader, cache)

    }

    override fun handles(model: RecipeSummaryEntity): Boolean = true

    override fun getUrl(
        model: RecipeSummaryEntity?,
        width: Int,
        height: Int,
        options: Options?
    ): String? {
        logger.v { "getUrl() called with: model = $model, width = $width, height = $height, options = $options" }
        return runBlocking { recipeImageUrlProvider.generateImageUrl(model?.slug) }
    }
}