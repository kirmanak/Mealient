package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.add.AddRecipeIngredientInfo
import gq.kirmanak.mealient.data.add.AddRecipeInstructionInfo
import gq.kirmanak.mealient.data.add.AddRecipeSettingsInfo
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeIngredientInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeInstructionInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSettingsInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeIngredientV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeInstructionV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeSettingsV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeIngredientResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeInstructionResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeIngredientV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeInstructionV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeSettingsV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeIngredientResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeInstructionResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSettingsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
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

    val RECIPE_SUMMARY_PORRIDGE_V0 = RecipeSummaryInfo(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "porridge",
    )

    val RECIPE_SUMMARY_PORRIDGE_V1 = RecipeSummaryInfo(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        imageId = "2",
    )

    val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE_V0)

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

    val TEST_RECIPE_SUMMARY_ENTITIES =
        listOf(CAKE_RECIPE_SUMMARY_ENTITY, PORRIDGE_RECIPE_SUMMARY_ENTITY)

    val SUGAR_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white sugar",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    val BREAD_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white bread",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    private val MILK_INGREDIENT = RecipeIngredientInfo(
        note = "2 oz of white milk",
        quantity = 1.0,
        unit = null,
        food = null,
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

    val CAKE_FULL_RECIPE_INFO = FullRecipeInfo(
        remoteId = "1",
        name = "Cake",
        recipeYield = "4 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, BREAD_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BAKE_INSTRUCTION),
        settings = RecipeSettingsInfo(disableAmounts = true)
    )

    val PORRIDGE_FULL_RECIPE_INFO = FullRecipeInfo(
        remoteId = "2",
        name = "Porridge",
        recipeYield = "3 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, MILK_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BOIL_INSTRUCTION),
        settings = RecipeSettingsInfo(disableAmounts = true)
    )

    val MIX_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        recipeId = "1",
        text = "Mix the ingredients",
    )

    private val BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        recipeId = "1",
        text = "Bake the ingredients",
    )

    val CAKE_RECIPE_ENTITY = RecipeEntity(
        remoteId = "1",
        recipeYield = "4 servings",
        disableAmounts = true,
    )

    val CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        recipeId = "1",
        note = "2 oz of white sugar",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    val CAKE_BREAD_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        recipeId = "1",
        note = "2 oz of white bread",
        quantity = 1.0,
        unit = null,
        food = null,
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
        recipeYield = "3 servings",
        disableAmounts = true,
    )

    private val PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        recipeId = "2",
        note = "2 oz of white milk",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    private val PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        recipeId = "2",
        note = "2 oz of white sugar",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    private val PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        recipeId = "2",
        text = "Mix the ingredients"
    )

    private val PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
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

    val SUGAR_ADD_RECIPE_INGREDIENT_INFO = AddRecipeIngredientInfo("2 oz of white sugar")

    val MILK_ADD_RECIPE_INGREDIENT_INFO = AddRecipeIngredientInfo("2 oz of white milk")

    val BOIL_ADD_RECIPE_INSTRUCTION_INFO = AddRecipeInstructionInfo("Boil the ingredients")

    val MIX_ADD_RECIPE_INSTRUCTION_INFO = AddRecipeInstructionInfo("Mix the ingredients")

    val ADD_RECIPE_INFO_SETTINGS = AddRecipeSettingsInfo(disableComments = false, public = true)

    val PORRIDGE_ADD_RECIPE_INFO = AddRecipeInfo(
        name = "Porridge",
        description = "A tasty porridge",
        recipeYield = "3 servings",
        recipeIngredient = listOf(
            MILK_ADD_RECIPE_INGREDIENT_INFO,
            SUGAR_ADD_RECIPE_INGREDIENT_INFO,
        ),
        recipeInstructions = listOf(
            MIX_ADD_RECIPE_INSTRUCTION_INFO,
            BOIL_ADD_RECIPE_INSTRUCTION_INFO,
        ),
        settings = ADD_RECIPE_INFO_SETTINGS,
    )

    val PORRIDGE_RECIPE_DRAFT = AddRecipeDraft(
        recipeName = "Porridge",
        recipeDescription = "A tasty porridge",
        recipeYield = "3 servings",
        recipeInstructions = listOf("Mix the ingredients", "Boil the ingredients"),
        recipeIngredients = listOf("2 oz of white milk", "2 oz of white sugar"),
        isRecipePublic = true,
        areCommentsDisabled = false,
    )

    val PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0 = GetRecipeSummaryResponseV0(
        remoteId = 2,
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    )

    val PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1 = GetRecipeSummaryResponseV1(
        remoteId = "2",
        name = "Porridge",
        slug = "porridge",
        description = "A tasty porridge",
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    )

    val VERSION_RESPONSE_V0 = VersionResponseV0("v0.5.6")

    val VERSION_INFO_V0 = VersionInfo("v0.5.6")

    val VERSION_RESPONSE_V1 = VersionResponseV1("v1.0.0-beta05")

    val VERSION_INFO_V1 = VersionInfo("v1.0.0-beta05")

    val MILK_RECIPE_INGREDIENT_RESPONSE_V0 = GetRecipeIngredientResponseV0("2 oz of white milk")

    val SUGAR_RECIPE_INGREDIENT_RESPONSE_V0 = GetRecipeIngredientResponseV0("2 oz of white sugar")

    val MILK_RECIPE_INGREDIENT_RESPONSE_V1 = GetRecipeIngredientResponseV1(
        note = "2 oz of white milk",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    val SUGAR_RECIPE_INGREDIENT_RESPONSE_V1 = GetRecipeIngredientResponseV1(
        note = "2 oz of white sugar",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    val MILK_RECIPE_INGREDIENT_INFO = RecipeIngredientInfo(
        note = "2 oz of white milk",
        quantity = 1.0,
        unit = null,
        food = null,
    )

    val MIX_RECIPE_INSTRUCTION_RESPONSE_V0 = GetRecipeInstructionResponseV0("Mix the ingredients")

    val BOIL_RECIPE_INSTRUCTION_RESPONSE_V0 = GetRecipeInstructionResponseV0("Boil the ingredients")

    val MIX_RECIPE_INSTRUCTION_RESPONSE_V1 = GetRecipeInstructionResponseV1("Mix the ingredients")

    val BOIL_RECIPE_INSTRUCTION_RESPONSE_V1 = GetRecipeInstructionResponseV1("Boil the ingredients")

    val MIX_RECIPE_INSTRUCTION_INFO = RecipeInstructionInfo("Mix the ingredients")

    val PORRIDGE_RECIPE_RESPONSE_V0 = GetRecipeResponseV0(
        remoteId = 2,
        name = "Porridge",
        recipeYield = "3 servings",
        recipeIngredients = listOf(
            SUGAR_RECIPE_INGREDIENT_RESPONSE_V0,
            MILK_RECIPE_INGREDIENT_RESPONSE_V0,
        ),
        recipeInstructions = listOf(
            MIX_RECIPE_INSTRUCTION_RESPONSE_V0,
            BOIL_RECIPE_INSTRUCTION_RESPONSE_V0
        ),
    )

    val PORRIDGE_RECIPE_RESPONSE_V1 = GetRecipeResponseV1(
        remoteId = "2",
        name = "Porridge",
        recipeYield = "3 servings",
        recipeIngredients = listOf(
            SUGAR_RECIPE_INGREDIENT_RESPONSE_V1,
            MILK_RECIPE_INGREDIENT_RESPONSE_V1,
        ),
        recipeInstructions = listOf(
            MIX_RECIPE_INSTRUCTION_RESPONSE_V1,
            BOIL_RECIPE_INSTRUCTION_RESPONSE_V1
        ),
        settings = GetRecipeSettingsResponseV1(disableAmount = true),
    )

    val MIX_ADD_RECIPE_INSTRUCTION_REQUEST_V0 = AddRecipeInstructionV0("Mix the ingredients")

    val BOIL_ADD_RECIPE_INSTRUCTION_REQUEST_V0 = AddRecipeInstructionV0("Boil the ingredients")

    val SUGAR_ADD_RECIPE_INGREDIENT_REQUEST_V0 = AddRecipeIngredientV0("2 oz of white sugar")

    val MILK_ADD_RECIPE_INGREDIENT_REQUEST_V0 = AddRecipeIngredientV0("2 oz of white milk")

    val ADD_RECIPE_REQUEST_SETTINGS_V0 = AddRecipeSettingsV0(disableComments = false, public = true)

    val PORRIDGE_ADD_RECIPE_REQUEST_V0 = AddRecipeRequestV0(
        name = "Porridge",
        description = "A tasty porridge",
        recipeYield = "3 servings",
        recipeInstructions = listOf(
            MIX_ADD_RECIPE_INSTRUCTION_REQUEST_V0,
            BOIL_ADD_RECIPE_INSTRUCTION_REQUEST_V0,
        ),
        recipeIngredient = listOf(
            MILK_ADD_RECIPE_INGREDIENT_REQUEST_V0,
            SUGAR_ADD_RECIPE_INGREDIENT_REQUEST_V0,
        ),
        settings = ADD_RECIPE_REQUEST_SETTINGS_V0
    )

    val MIX_ADD_RECIPE_INSTRUCTION_REQUEST_V1 = AddRecipeInstructionV1(
        id = "1",
        text = "Mix the ingredients",
        ingredientReferences = emptyList()
    )

    val BOIL_ADD_RECIPE_INSTRUCTION_REQUEST_V1 = AddRecipeInstructionV1(
        id = "2",
        text = "Boil the ingredients",
        ingredientReferences = emptyList()
    )

    val SUGAR_ADD_RECIPE_INGREDIENT_REQUEST_V1 = AddRecipeIngredientV1(
        id = "3",
        note = "2 oz of white sugar"
    )

    val MILK_ADD_RECIPE_INGREDIENT_REQUEST_V1 = AddRecipeIngredientV1(
        id = "4",
        note = "2 oz of white milk"
    )

    val ADD_RECIPE_REQUEST_SETTINGS_V1 = AddRecipeSettingsV1(disableComments = false, public = true)

    val PORRIDGE_CREATE_RECIPE_REQUEST_V1 = CreateRecipeRequestV1(name = "Porridge")

    val PORRIDGE_UPDATE_RECIPE_REQUEST_V1 = UpdateRecipeRequestV1(
        description = "A tasty porridge",
        recipeYield = "3 servings",
        recipeInstructions = listOf(
            MIX_ADD_RECIPE_INSTRUCTION_REQUEST_V1,
            BOIL_ADD_RECIPE_INSTRUCTION_REQUEST_V1,
        ),
        recipeIngredient = listOf(
            MILK_ADD_RECIPE_INGREDIENT_REQUEST_V1,
            SUGAR_ADD_RECIPE_INGREDIENT_REQUEST_V1,
        ),
        settings = ADD_RECIPE_REQUEST_SETTINGS_V1
    )
}