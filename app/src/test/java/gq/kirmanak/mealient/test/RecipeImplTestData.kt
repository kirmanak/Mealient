package gq.kirmanak.mealient.test

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
        image = "86",
        description = "A tasty cake",
        recipeCategories = listOf("dessert", "tasty"),
        tags = listOf("gluten", "allergic"),
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        imageId = "cake",
    )

    val RECIPE_SUMMARY_PORRIDGE = RecipeSummaryInfo(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        recipeCategories = listOf("porridge", "tasty"),
        tags = listOf("gluten", "milk"),
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "porridge",
    )

    val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE)

    val CAKE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = "1",
        name = "Cake",
        slug = "cake",
        image = "86",
        description = "A tasty cake",
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        imageId = "cake",
    )

    val PORRIDGE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "porridge",
    )

    private val SUGAR_INGREDIENT = RecipeIngredientInfo(
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1.0
    )

    val BREAD_INGREDIENT = RecipeIngredientInfo(
        title = "Bread",
        note = "2 oz of white bread",
        unit = "",
        food = "",
        disableAmount = false,
        quantity = 2.0
    )

    private val MILK_INGREDIENT = RecipeIngredientInfo(
        title = "Milk",
        note = "2 oz of white milk",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 3.0
    )

    val MIX_INSTRUCTION = RecipeInstructionInfo(
        title = "Mix",
        text = "Mix the ingredients"
    )

    private val BAKE_INSTRUCTION = RecipeInstructionInfo(
        title = "Bake",
        text = "Bake the ingredients"
    )

    private val BOIL_INSTRUCTION = RecipeInstructionInfo(
        title = "Boil",
        text = "Boil the ingredients"
    )

    val GET_CAKE_RESPONSE = FullRecipeInfo(
        remoteId = "1",
        name = "Cake",
        slug = "cake",
        image = "86",
        description = "A tasty cake",
        recipeCategories = listOf("dessert", "tasty"),
        tags = listOf("gluten", "allergic"),
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        recipeYield = "4 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, BREAD_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BAKE_INSTRUCTION)
    )

    val GET_PORRIDGE_RESPONSE = FullRecipeInfo(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        recipeCategories = listOf("porridge", "tasty"),
        tags = listOf("gluten", "milk"),
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        recipeYield = "3 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, MILK_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BOIL_INSTRUCTION)
    )

    val MIX_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 1,
        recipeId = "1",
        title = "Mix",
        text = "Mix the ingredients",
    )

    private val BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 2,
        recipeId = "1",
        title = "Bake",
        text = "Bake the ingredients",
    )

    private val CAKE_RECIPE_ENTITY = RecipeEntity(
        remoteId = "1",
        recipeYield = "4 servings"
    )

    private val CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 1,
        recipeId = "1",
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1.0
    )

    val CAKE_BREAD_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 2,
        recipeId = "1",
        title = "Bread",
        note = "2 oz of white bread",
        unit = "",
        food = "",
        disableAmount = false,
        quantity = 2.0
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
        title = "Milk",
        note = "2 oz of white milk",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 3.0
    )

    private val PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 3,
        recipeId = "2",
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1.0
    )

    private val PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 3,
        recipeId = "2",
        title = "Mix",
        text = "Mix the ingredients"
    )

    private val PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 4,
        recipeId = "2",
        title = "Boil",
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
}