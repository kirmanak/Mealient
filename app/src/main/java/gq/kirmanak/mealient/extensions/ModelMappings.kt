package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

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
    title = title,
)

fun RecipeInstructionInfo.toRecipeInstructionEntity(remoteId: String) = RecipeInstructionEntity(
    recipeId = remoteId,
    text = text
)

fun RecipeSummaryInfo.toRecipeSummaryEntity(isFavorite: Boolean) = RecipeSummaryEntity(
    remoteId = remoteId,
    name = name,
    slug = slug,
    description = description,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = imageId,
    isFavorite = isFavorite,
)

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


