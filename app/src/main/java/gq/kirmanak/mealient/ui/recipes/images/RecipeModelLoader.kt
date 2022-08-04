package gq.kirmanak.mealient.ui.recipes.images

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.InputStream

class RecipeModelLoader(
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    concreteLoader: ModelLoader<GlideUrl, InputStream>,
    cache: ModelCache<RecipeSummaryEntity, GlideUrl>,
) : BaseGlideUrlLoader<RecipeSummaryEntity>(concreteLoader, cache) {

    override fun handles(model: RecipeSummaryEntity): Boolean = true

    override fun getUrl(
        model: RecipeSummaryEntity?,
        width: Int,
        height: Int,
        options: Options?
    ): String? {
        Timber.v("getUrl() called with: model = $model, width = $width, height = $height, options = $options")
        return runBlocking { recipeImageUrlProvider.generateImageUrl(model?.slug) }
    }
}