package gq.kirmanak.mealient.ui.recipes

import android.widget.ImageView

interface RecipeImageLoader {

    fun loadRecipeImage(view: ImageView, slug: String?)
}