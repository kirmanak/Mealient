package gq.kirmanak.mealient.ui.recipes.list

import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

data class RecipeListItemState(
    val imageUrl: String?,
    val entity: RecipeSummaryEntity,
)
