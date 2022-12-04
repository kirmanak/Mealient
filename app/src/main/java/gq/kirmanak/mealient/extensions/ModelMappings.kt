package gq.kirmanak.mealient.extensions

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
import gq.kirmanak.mealient.data.share.ParseRecipeURLInfo
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
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
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
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
import java.util.*

fun FullRecipeInfo.toRecipeEntity() = RecipeEntity(
    remoteId = remoteId,
    recipeYield = recipeYield,
    disableAmounts = settings.disableAmounts,
)

fun RecipeIngredientInfo.toRecipeIngredientEntity(remoteId: String) = RecipeIngredientEntity(
    recipeId = remoteId,
    note = note,
    unit = unit,
    food = food,
    quantity = quantity,
)

fun RecipeInstructionInfo.toRecipeInstructionEntity(remoteId: String) = RecipeInstructionEntity(
    recipeId = remoteId,
    text = text
)

fun GetRecipeSummaryResponseV0.toRecipeSummaryInfo() = RecipeSummaryInfo(
    remoteId = remoteId.toString(),
    name = name,
    slug = slug,
    description = description,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = slug,
)

fun GetRecipeSummaryResponseV1.toRecipeSummaryInfo() = RecipeSummaryInfo(
    remoteId = remoteId,
    name = name,
    slug = slug,
    description = description,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = remoteId,
)

fun RecipeSummaryInfo.toRecipeSummaryEntity() = RecipeSummaryEntity(
    remoteId = remoteId,
    name = name,
    slug = slug,
    description = description,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = imageId,
)

fun VersionResponseV0.toVersionInfo() = VersionInfo(version)

fun VersionResponseV1.toVersionInfo() = VersionInfo(version)

fun AddRecipeDraft.toAddRecipeInfo() = AddRecipeInfo(
    name = recipeName,
    description = recipeDescription,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredients.map { AddRecipeIngredientInfo(note = it) },
    recipeInstructions = recipeInstructions.map { AddRecipeInstructionInfo(text = it) },
    settings = AddRecipeSettingsInfo(
        public = isRecipePublic,
        disableComments = areCommentsDisabled,
    )
)

fun AddRecipeInfo.toDraft(): AddRecipeDraft = AddRecipeDraft(
    recipeName = name,
    recipeDescription = description,
    recipeYield = recipeYield,
    recipeInstructions = recipeInstructions.map { it.text },
    recipeIngredients = recipeIngredient.map { it.note },
    isRecipePublic = settings.public,
    areCommentsDisabled = settings.disableComments,
)

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
)

fun GetRecipeInstructionResponseV0.toRecipeInstructionInfo() = RecipeInstructionInfo(
    text = text
)

fun GetRecipeResponseV1.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId,
    name = name,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() },
    settings = settings.toRecipeSettingsInfo(),
)

private fun GetRecipeSettingsResponseV1.toRecipeSettingsInfo() = RecipeSettingsInfo(
    disableAmounts = disableAmount,
)

fun GetRecipeIngredientResponseV1.toRecipeIngredientInfo() = RecipeIngredientInfo(
    note = note,
    unit = unit?.name,
    food = food?.name,
    quantity = quantity,
)

fun GetRecipeInstructionResponseV1.toRecipeInstructionInfo() = RecipeInstructionInfo(
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


fun AddRecipeInfo.toV1CreateRequest() = CreateRecipeRequestV1(
    name = name,
)

fun AddRecipeInfo.toV1UpdateRequest() = UpdateRecipeRequestV1(
    description = description,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredient.map { it.toV1Ingredient() },
    recipeInstructions = recipeInstructions.map { it.toV1Instruction() },
    settings = settings.toV1Settings(),
)

private fun AddRecipeSettingsInfo.toV1Settings() = AddRecipeSettingsV1(
    disableComments = disableComments,
    public = public,
)

private fun AddRecipeIngredientInfo.toV1Ingredient() = AddRecipeIngredientV1(
    id = UUID.randomUUID().toString(),
    note = note,
)

private fun AddRecipeInstructionInfo.toV1Instruction() = AddRecipeInstructionV1(
    id = UUID.randomUUID().toString(),
    text = text,
    ingredientReferences = emptyList(),
)

fun ParseRecipeURLInfo.toV1Request() = ParseRecipeURLRequestV1(
    url = url,
    includeTags = includeTags,
)

fun ParseRecipeURLInfo.toV0Request() = ParseRecipeURLRequestV0(
    url = url,
)
