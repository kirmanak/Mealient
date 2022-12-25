package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.models.VersionInfo
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeIngredientV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeInstructionV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeSettingsV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeIngredientResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeInstructionResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0

fun GetRecipeSummaryResponseV0.toRecipeSummaryInfo() = RecipeSummaryInfo(
    remoteId = remoteId.toString(),
    name = name,
    slug = slug,
    description = description,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = slug,
)

fun VersionResponseV0.toVersionInfo() = VersionInfo(version)

fun GetRecipeResponseV0.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId.toString(),
    name = name,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() },
    settings = RecipeSettingsInfo(disableAmounts = true)
)

fun GetRecipeIngredientResponseV0.toRecipeIngredientInfo() = RecipeIngredientInfo(
    note = note,
    unit = null,
    food = null,
    quantity = 1.0,
    title = null,
)

fun GetRecipeInstructionResponseV0.toRecipeInstructionInfo() = RecipeInstructionInfo(
    text = text
)

fun AddRecipeInfo.toV0Request() = AddRecipeRequestV0(
    name = name,
    description = description,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredient.map { it.toV0Ingredient() },
    recipeInstructions = recipeInstructions.map { it.toV0Instruction() },
    settings = settings.toV0Settings(),
)

private fun AddRecipeSettingsInfo.toV0Settings() = AddRecipeSettingsV0(
    disableComments = disableComments,
    public = public,
)

private fun AddRecipeIngredientInfo.toV0Ingredient() = AddRecipeIngredientV0(
    note = note,
)

private fun AddRecipeInstructionInfo.toV0Instruction() = AddRecipeInstructionV0(
    text = text,
)

fun ParseRecipeURLInfo.toV0Request() = ParseRecipeURLRequestV0(
    url = url,
)
