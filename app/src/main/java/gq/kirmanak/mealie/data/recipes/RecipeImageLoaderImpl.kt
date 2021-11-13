package gq.kirmanak.mealie.data.recipes

import android.widget.ImageView
import gq.kirmanak.mealie.R
import gq.kirmanak.mealie.data.auth.AuthRepo
import gq.kirmanak.mealie.ui.ImageLoader
import javax.inject.Inject

class RecipeImageLoaderImpl @Inject constructor(
    private val imageLoader: ImageLoader,
    private val authRepo: AuthRepo
): RecipeImageLoader {
    override suspend fun loadRecipeImage(view: ImageView, slug: String?) {
        val baseUrl = authRepo.getBaseUrl()
        val recipeImageUrl =
            if (baseUrl.isNullOrBlank()) null
            else "$baseUrl/api/media/recipes/$slug/images/original.webp"
        imageLoader.loadImage(recipeImageUrl, R.drawable.placeholder_recipe, view)
    }
}