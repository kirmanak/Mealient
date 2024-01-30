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
import gq.kirmanak.mealient.datasource.models.GetRecipeInstructionIngredientReference
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
    rating = null,
)

val RECIPE_SUMMARY_PORRIDGE = GetRecipeSummaryResponse(
    remoteId = "2",
    name = "Porridge",
    slug = "porridge",
    description = "A tasty porridge",
    dateAdded = LocalDate.parse("2021-11-12"),
    dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    rating = null
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
    rating = null
)

val MILK_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white milk",
    unit = null,
    food = null,
    quantity = 1.0,
    display = "2 oz of white milk",
    referenceId = "1",
    title = null,
    isFood = false,
    disableAmount = true,
)

val SUGAR_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white sugar",
    unit = null,
    food = null,
    quantity = 1.0,
    display = "2 oz of white sugar",
    referenceId = "1",
    title = "Sugar",
    isFood = false,
    disableAmount = true,
)

val BREAD_RECIPE_INGREDIENT_RESPONSE = GetRecipeIngredientResponse(
    note = "2 oz of white bread",
    unit = null,
    food = null,
    quantity = 1.0,
    display = "2 oz of white bread",
    referenceId = "3",
    title = null,
    isFood = false,
    disableAmount = true,
)

val MIX_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse(
    id = "1",
    title = "",
    text = "Mix the ingredients",
    ingredientReferences = listOf(
        GetRecipeInstructionIngredientReference(referenceId = "1"),
        GetRecipeInstructionIngredientReference(referenceId = "3"),
    ),
)

val BAKE_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse(
    id = "2",
    title = "",
    text = "Bake the ingredients",
    ingredientReferences = emptyList()
)

val BOIL_RECIPE_INSTRUCTION_RESPONSE = GetRecipeInstructionResponse(
    id = "3",
    title = "",
    text = "Boil the ingredients",
    ingredientReferences = emptyList()
)

val NO_AMOUNT_RECIPE_SETTINGS_RESPONSE = GetRecipeSettingsResponse(
    disableAmount = true,
    locked = false,
    public = false,
    disableComments = false,
    showAssets = true,
    showNutrition = true,
    landscapeView = false
)

val CAKE_RECIPE_RESPONSE = GetRecipeResponse(
    remoteId = "1",
    name = "Cake",
    recipeYield = "4 servings",
    ingredients = listOf(SUGAR_RECIPE_INGREDIENT_RESPONSE, BREAD_RECIPE_INGREDIENT_RESPONSE),
    instructions = listOf(MIX_RECIPE_INSTRUCTION_RESPONSE, BAKE_RECIPE_INSTRUCTION_RESPONSE),
    groupId = "123094870213",
    userId = "12374879",
    settings = NO_AMOUNT_RECIPE_SETTINGS_RESPONSE,
)

val PORRIDGE_RECIPE_RESPONSE = GetRecipeResponse(
    remoteId = "2",
    recipeYield = "3 servings",
    name = "Porridge",
    groupId = "123094870213",
    userId = "12374879",
    ingredients = listOf(
        SUGAR_RECIPE_INGREDIENT_RESPONSE,
        MILK_RECIPE_INGREDIENT_RESPONSE,
    ),
    instructions = listOf(
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
