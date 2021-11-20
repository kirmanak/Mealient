package gq.kirmanak.mealient.data.recipes

import android.widget.ImageView

interface RecipeImageLoader {
    suspend fun loadRecipeImage(view: ImageView, slug: String?)
}