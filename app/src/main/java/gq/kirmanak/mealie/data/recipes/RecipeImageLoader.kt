package gq.kirmanak.mealie.data.recipes

import android.widget.ImageView

interface RecipeImageLoader {
    suspend fun loadRecipeImage(view: ImageView, slug: String?)
}