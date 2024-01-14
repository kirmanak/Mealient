package gq.kirmanak.mealient.database

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientToInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeWithSummaryAndIngredientsAndInstructions
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

val CAKE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
    remoteId = "1",
    name = "Cake",
    slug = "cake",
    description = "A tasty cake",
    dateAdded = LocalDate.parse("2021-11-13"),
    dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
    imageId = "1",
    isFavorite = false,
)

val PORRIDGE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
    remoteId = "2",
    name = "Porridge",
    slug = "porridge",
    description = "A tasty porridge",
    dateAdded = LocalDate.parse("2021-11-12"),
    dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    imageId = "2",
    isFavorite = false,
)

val TEST_RECIPE_SUMMARY_ENTITIES =
    listOf(CAKE_RECIPE_SUMMARY_ENTITY, PORRIDGE_RECIPE_SUMMARY_ENTITY)

val MIX_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
    id = "1",
    recipeId = "1",
    text = "Mix the ingredients",
    title = "",
)

val BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
    id = "2",
    recipeId = "1",
    text = "Bake the ingredients",
    title = "",
)

val MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY = RecipeIngredientToInstructionEntity(
    recipeId = "1",
    ingredientId = "1",
    instructionId = "1",
)

val MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY = RecipeIngredientToInstructionEntity(
    recipeId = "1",
    ingredientId = "3",
    instructionId = "1",
)

val CAKE_RECIPE_ENTITY = RecipeEntity(
    remoteId = "1",
    recipeYield = "4 servings",
    disableAmounts = true,
)

val CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
    id = "1",
    recipeId = "1",
    note = "2 oz of white sugar",
    food = null,
    unit = null,
    quantity = 1.0,
    display = "2 oz of white sugar",
    title = "Sugar",
    isFood = false,
    disableAmount = true,
)

val CAKE_BREAD_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
    id = "3",
    recipeId = "1",
    note = "2 oz of white bread",
    food = null,
    unit = null,
    quantity = 1.0,
    display = "2 oz of white bread",
    title = null,
    isFood = false,
    disableAmount = true,
)

val FULL_CAKE_INFO_ENTITY = RecipeWithSummaryAndIngredientsAndInstructions(
    recipeEntity = CAKE_RECIPE_ENTITY,
    recipeSummaryEntity = CAKE_RECIPE_SUMMARY_ENTITY,
    recipeIngredients = listOf(
        CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY,
        CAKE_BREAD_RECIPE_INGREDIENT_ENTITY,
    ),
    recipeInstructions = listOf(
        MIX_CAKE_RECIPE_INSTRUCTION_ENTITY,
        BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY,
    ),
    recipeIngredientToInstructionEntity = listOf(
        MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY,
        MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY
    ),
)

val PORRIDGE_RECIPE_ENTITY_FULL = RecipeEntity(
    remoteId = "2",
    recipeYield = "3 servings",
    disableAmounts = true,
)

val PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
    id = "1",
    recipeId = "2",
    note = "2 oz of white milk",
    food = null,
    unit = null,
    quantity = 1.0,
    display = "2 oz of white milk",
    title = null,
    isFood = false,
    disableAmount = true,
)

val PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
    id = "2",
    recipeId = "2",
    note = "2 oz of white sugar",
    food = null,
    unit = null,
    quantity = 1.0,
    display = "2 oz of white sugar",
    title = "Sugar",
    isFood = false,
    disableAmount = true,
)

val PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
    id = "1",
    recipeId = "2",
    text = "Mix the ingredients",
    title = "",
)

val PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
    id = "2",
    recipeId = "2",
    text = "Boil the ingredients",
    title = "",
)

val FULL_PORRIDGE_INFO_ENTITY = RecipeWithSummaryAndIngredientsAndInstructions(
    recipeEntity = PORRIDGE_RECIPE_ENTITY_FULL,
    recipeSummaryEntity = PORRIDGE_RECIPE_SUMMARY_ENTITY,
    recipeIngredients = listOf(
        PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY,
        PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY,
    ),
    recipeInstructions = listOf(
        PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY,
        PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY,
    ),
    recipeIngredientToInstructionEntity = emptyList(),
)
