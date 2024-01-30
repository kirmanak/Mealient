package gq.kirmanak.mealient.ui.recipes.info

import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

internal val INGREDIENT_TWO = RecipeIngredientEntity(
    id = "2",
    recipeId = "1",
    note = "Recipe ingredient note",
    food = "Recipe ingredient food",
    unit = "Recipe ingredient unit",
    quantity = 1.0,
    display = "Recipe ingredient display that is very long and should be wrapped",
    title = null,
    isFood = false,
    disableAmount = true,
)

internal val SUMMARY_ENTITY = RecipeSummaryEntity(
    remoteId = "1",
    name = "Recipe name",
    slug = "recipe-name",
    description = "Recipe description",
    dateAdded = LocalDate(2021, 1, 1),
    dateUpdated = LocalDateTime(2021, 1, 1, 1, 1, 1),
    imageId = null,
    isFavorite = false,
    rating = 3,
)

internal val INGREDIENT_ONE = RecipeIngredientEntity(
    id = "1",
    recipeId = "1",
    note = "Recipe ingredient note",
    food = "Recipe ingredient food",
    unit = "Recipe ingredient unit",
    quantity = 1.0,
    display = "Recipe ingredient display that is very long and should be wrapped",
    title = "Recipe ingredient section title",
    isFood = false,
    disableAmount = true,
)

internal val INSTRUCTION_ONE = RecipeInstructionEntity(
    id = "1",
    recipeId = "1",
    text = "Recipe instruction",
    title = "Section title",
)

internal val INSTRUCTION_TWO = RecipeInstructionEntity(
    id = "2",
    recipeId = "1",
    text = "Recipe instruction",
    title = "",
)

internal val INGREDIENTS = listOf(
    INGREDIENT_ONE,
    INGREDIENT_TWO,
)

internal val INSTRUCTIONS = mapOf(
    INSTRUCTION_ONE to emptyList(),
    INSTRUCTION_TWO to listOf(INGREDIENT_TWO),
)
