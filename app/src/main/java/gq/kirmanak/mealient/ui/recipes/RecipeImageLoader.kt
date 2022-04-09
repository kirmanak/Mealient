package gq.kirmanak.mealient.ui.recipes

import android.widget.ImageView

interface RecipeImageLoader {
    suspend fun loadRecipeImage(view: ImageView, slug: String?)
}