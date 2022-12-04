package gq.kirmanak.mealient.ui.recipes.info

import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

data class RecipeInfoUiState(
    val showIngredients: Boolean = false,
    val showInstructions: Boolean = false,
    val summaryEntity: RecipeSummaryEntity? = null,
    val recipeIngredients: List<RecipeIngredientEntity> = emptyList(),
    val recipeInstructions: List<RecipeInstructionEntity> = emptyList(),
    val title: String? = null,
    val description: String? = null,
    val disableAmounts: Boolean = true,
)
