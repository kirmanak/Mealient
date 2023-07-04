package gq.kirmanak.mealient.ui.recipes.images

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.runBlocking
import java.io.InputStream

class RecipeModelLoader @AssistedInject constructor(
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    private val logger: Logger,
    @Assisted concreteLoader: ModelLoader<GlideUrl, InputStream>,
    @Assisted cache: ModelCache<RecipeSummaryEntity, GlideUrl>,
) : BaseGlideUrlLoader<RecipeSummaryEntity>(concreteLoader, cache) {

    @AssistedFactory
    interface Factory {

        fun build(
            concreteLoader: ModelLoader<GlideUrl, InputStream>,
            cache: ModelCache<RecipeSummaryEntity, GlideUrl>,
        ): RecipeModelLoader

    }

    override fun handles(model: RecipeSummaryEntity): Boolean = true

    override fun getUrl(
        model: RecipeSummaryEntity?,
        width: Int,
        height: Int,
        options: Options?
    ): String? {
        logger.v { "getUrl() called with: model = $model, width = $width, height = $height, options = $options" }
        return runBlocking { recipeImageUrlProvider.generateImageUrl(model?.imageId) }
    }
}