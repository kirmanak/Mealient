package gq.kirmanak.mealient.ui.recipes.info

import gq.kirmanak.mealient.data.recipes.impl.FullRecipeInfo

data class RecipeInfoUiState(
    val areIngredientsVisible: Boolean = false,
    val areInstructionsVisible: Boolean = false,
    val recipeInfo: FullRecipeInfo? = null,
)
