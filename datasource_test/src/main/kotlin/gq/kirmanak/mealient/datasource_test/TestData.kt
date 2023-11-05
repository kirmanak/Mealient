package gq.kirmanak.mealient.datasource_test

import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredient
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstruction
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettings
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.CreateRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeIngredientResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeInstructionResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSettingsResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

val RECIPE_SUMMARY_CAKE = GetRecipeSummaryResponse(
    remoteId = "1",
    name = "Cake",
    slug = "cake",
    description = "A tasty cake",
    dateAdded = LocalDate.parse("2021-11-13"),
    dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
)

val RECIPE_SUMMARY_PORRIDGE = GetRecipeSummaryResponse(
    remoteId = "2",
    name = "Porridge",
    slug = "porridge",
    description = "A tasty porridge",
    dateAdded = LocalDate.parse("2021-11-12"),
    dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
)

val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE)

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

val PORRIDGE_RECIPE_SUMMARY_RESPONSE = GetRecipeSummaryResponse(
    remoteId = "2",
    name = "Porridge",
    slug = "porridge",
    description = "A tasty porridge",
    dateAdded = LocalDate.parse("2021-11-12"),
    dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
)

val MILK_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white milk",
    quantity = 1.0,
    unit = null,
    food = null,
    title = null,
)

val SUGAR_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white sugar",
    quantity = 1.0,
    unit = null,
    food = null,
    title = null,
)

val BREAD_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white bread",
    quantity = 1.0,
    unit = null,
    food = null,
    title = null,
)

val MIX_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse("Mix the ingredients")

val BAKE_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse("Bake the ingredients")

val BOIL_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse("Boil the ingredients")

val NO_AMOUNT_RECIPE_SETTINGS_RESPONSE = GetRecipeSettingsResponse(disableAmount = true)

val CAKE_RECIPE_RESPONSE = GetRecipeResponse(
    remoteId = "1",
    name = "Cake",
    recipeYield = "4 servings",
    recipeIngredients = listOf(SUGAR_RECIPE_INGREDIENT_RESPONSE, BREAD_RECIPE_INGREDIENT_RESPONSE),
    recipeInstructions = listOf(MIX_RECIPE_INSTRUCTION_RESPONSE, BAKE_RECIPE_INSTRUCTION_RESPONSE),
    settings = NO_AMOUNT_RECIPE_SETTINGS_RESPONSE,
)

val PORRIDGE_RECIPE_RESPONSE = GetRecipeResponse(
    remoteId = "2",
    recipeYield = "3 servings",
    name = "Porridge",
    recipeIngredients = listOf(
        SUGAR_RECIPE_INGREDIENT_RESPONSE,
        MILK_RECIPE_INGREDIENT_RESPONSE,
    ),
    recipeInstructions = listOf(
        MIX_RECIPE_INSTRUCTION_RESPONSE,
        BOIL_RECIPE_INSTRUCTION_RESPONSE
    ),
)

val MIX_ADD_RECIPE_INSTRUCTION_REQUEST = AddRecipeInstruction(
    id = "1",
    text = "Mix the ingredients",
    ingredientReferences = emptyList()
)

val BOIL_ADD_RECIPE_INSTRUCTION_REQUEST = AddRecipeInstruction(
    id = "2",
    text = "Boil the ingredients",
    ingredientReferences = emptyList()
)

val SUGAR_ADD_RECIPE_INGREDIENT_REQUEST = AddRecipeIngredient(
    id = "3",
    note = "2 oz of white sugar"
)

val MILK_ADD_RECIPE_INGREDIENT_REQUEST = AddRecipeIngredient(
    id = "4",
    note = "2 oz of white milk"
)

val ADD_RECIPE_REQUEST_SETTINGS = AddRecipeSettings(disableComments = false, public = true)

val PORRIDGE_CREATE_RECIPE_REQUEST = CreateRecipeRequest(name = "Porridge")

val PORRIDGE_UPDATE_RECIPE_REQUEST = UpdateRecipeRequest(
    description = "A tasty porridge",
    recipeYield = "3 servings",
    recipeInstructions = listOf(
        MIX_ADD_RECIPE_INSTRUCTION_REQUEST,
        BOIL_ADD_RECIPE_INSTRUCTION_REQUEST,
    ),
    recipeIngredient = listOf(
        MILK_ADD_RECIPE_INGREDIENT_REQUEST,
        SUGAR_ADD_RECIPE_INGREDIENT_REQUEST,
    ),
    settings = ADD_RECIPE_REQUEST_SETTINGS
)
