package gq.kirmanak.mealient.ui.recipes.info

import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity

data class RecipeInfoUiState(
    val areIngredientsVisible: Boolean = false,
    val areInstructionsVisible: Boolean = false,
    val recipeInfo: FullRecipeEntity? = null,
)
