package gq.kirmanak.mealient.ui.recipes.images

import android.widget.ImageView
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeImageLoader {

    fun loadRecipeImage(view: ImageView, recipe: RecipeSummaryEntity?)
}