package gq.kirmanak.mealient.ui.recipes.images

import android.widget.ImageView
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity

interface RecipeImageLoader {

    fun loadRecipeImage(view: ImageView, recipe: RecipeSummaryEntity?)
}