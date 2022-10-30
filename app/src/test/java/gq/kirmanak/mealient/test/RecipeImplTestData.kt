package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.add.AddRecipeIngredientInfo
import gq.kirmanak.mealient.data.add.AddRecipeInstructionInfo
import gq.kirmanak.mealient.data.add.AddRecipeSettingsInfo
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeIngredientInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeInstructionInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

object RecipeImplTestData {
    val RECIPE_SUMMARY_CAKE = RecipeSummaryInfo(
        remoteId = "1",
        name = "Cake",
        slug = "cake",
        description = "A tasty cake",
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        imageId = "cake",
    )

    val RECIPE_SUMMARY_PORRIDGE = RecipeSummaryInfo(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "porridge",
    )

    val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE)

    val CAKE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = "1",
        name = "Cake",
        slug = "cake",
        description = "A tasty cake",
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        imageId = "cake",
    )

    val PORRIDGE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "porridge",
    )

    private val SUGAR_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white sugar",
    )

    val BREAD_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white bread",
    )

    private val MILK_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white milk",
    )

    val MIX_INSTRUCTION = RecipeInstructionInfo(
        text = "Mix the ingredients"
    )

    private val BAKE_INSTRUCTION = RecipeInstructionInfo(
        text = "Bake the ingredients"
    )

    private val BOIL_INSTRUCTION = RecipeInstructionInfo(
        text = "Boil the ingredients"
    )

    val GET_CAKE_RESPONSE = FullRecipeInfo(
        remoteId = "1",
        name = "Cake",
        recipeYield = "4 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, BREAD_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BAKE_INSTRUCTION)
    )

    val GET_PORRIDGE_RESPONSE = FullRecipeInfo(
        remoteId = "2",
        name = "Porridge",
        recipeYield = "3 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, MILK_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BOIL_INSTRUCTION)
    )

    val MIX_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 1,
        recipeId = "1",
        text = "Mix the ingredients",
    )

    private val BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 2,
        recipeId = "1",
        text = "Bake the ingredients",
    )

    private val CAKE_RECIPE_ENTITY = RecipeEntity(
        remoteId = "1",
        recipeYield = "4 servings"
    )

    private val CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 1,
        recipeId = "1",
        note = "2 oz of white sugar",
    )

    val CAKE_BREAD_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 2,
        recipeId = "1",
        note = "2 oz of white bread",
    )

    val FULL_CAKE_INFO_ENTITY = FullRecipeEntity(
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
    )

    private val PORRIDGE_RECIPE_ENTITY_FULL = RecipeEntity(
        remoteId = "2",
        recipeYield = "3 servings"
    )

    private val PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 4,
        recipeId = "2",
        note = "2 oz of white milk",
    )

    private val PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 3,
        recipeId = "2",
        note = "2 oz of white sugar",
    )

    private val PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 3,
        recipeId = "2",
        text = "Mix the ingredients"
    )

    private val PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 4,
        recipeId = "2",
        text = "Boil the ingredients"
    )

    val FULL_PORRIDGE_INFO_ENTITY = FullRecipeEntity(
        recipeEntity = PORRIDGE_RECIPE_ENTITY_FULL,
        recipeSummaryEntity = PORRIDGE_RECIPE_SUMMARY_ENTITY,
        recipeIngredients = listOf(
            PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY,
            PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY,
        ),
        recipeInstructions = listOf(
            PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY,
            PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY,
        )
    )

    val PORRIDGE_ADD_RECIPE_INFO = AddRecipeInfo(
        name = "Porridge",
        description = "Tasty breakfast",
        recipeYield = "5 servings",
        recipeIngredient = listOf(
            AddRecipeIngredientInfo("Milk"),
            AddRecipeIngredientInfo("Sugar"),
            AddRecipeIngredientInfo("Salt"),
            AddRecipeIngredientInfo("Porridge"),
        ),
        recipeInstructions = listOf(
            AddRecipeInstructionInfo("Mix"),
            AddRecipeInstructionInfo("Cook"),
        ),
        settings = AddRecipeSettingsInfo(disableComments = false, public = true),
    )
}