package gq.kirmanak.mealient.data.recipes.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class FullRecipeInfo(
    val remoteId: String,
    val name: String,
    val slug: String,
    val image: String,
    val description: String,
    val recipeCategories: List<String>,
    val tags: List<String>,
    val rating: Int?,
    val dateAdded: LocalDate,
    val dateUpdated: LocalDateTime,
    val recipeYield: String,
    val recipeIngredients: List<RecipeIngredientInfo>,
    val recipeInstructions: List<RecipeInstructionInfo>,
)

data class RecipeIngredientInfo(
    val title: String,
    val note: String,
    val unit: String,
    val food: String,
    val disableAmount: Boolean,
    val quantity: Double,
)

data class RecipeInstructionInfo(
    val title: String,
    val text: String,
)
