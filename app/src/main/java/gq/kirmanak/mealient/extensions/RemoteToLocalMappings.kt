package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.add.AddRecipeIngredientInfo
import gq.kirmanak.mealient.data.add.AddRecipeInstructionInfo
import gq.kirmanak.mealient.data.add.AddRecipeSettingsInfo
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeIngredientInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeInstructionInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.v0.models.*
import gq.kirmanak.mealient.datasource.v1.models.*
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

fun FullRecipeInfo.toRecipeEntity() = RecipeEntity(
    remoteId = remoteId,
    recipeYield = recipeYield
)

fun RecipeIngredientInfo.toRecipeIngredientEntity(remoteId: String) = RecipeIngredientEntity(
    recipeId = remoteId,
    note = note,
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

fun RecipeSummaryInfo.recipeEntity() = RecipeSummaryEntity(
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
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() }
)

fun GetRecipeIngredientResponseV0.toRecipeIngredientInfo() = RecipeIngredientInfo(
    note = note,
)

fun GetRecipeInstructionResponseV0.toRecipeInstructionInfo() = RecipeInstructionInfo(
    text = text
)

fun GetRecipeResponseV1.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId,
    name = name,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() }
)

fun GetRecipeIngredientResponseV1.toRecipeIngredientInfo() = RecipeIngredientInfo(
    note = note,
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
    note = note,
)

private fun AddRecipeInstructionInfo.toV1Instruction() = AddRecipeInstructionV1(
    text = text,
    ingredientReferences = emptyList(),
)