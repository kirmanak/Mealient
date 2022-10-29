package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.*
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

fun GetRecipeResponse.toRecipeEntity() = RecipeEntity(
    remoteId = remoteId,
    recipeYield = recipeYield
)

fun GetRecipeIngredientResponse.toRecipeIngredientEntity(remoteId: String) =
    RecipeIngredientEntity(
        recipeId = remoteId,
        title = title,
        note = note,
        unit = unit,
        food = food,
        disableAmount = disableAmount,
        quantity = quantity
    )

fun GetRecipeInstructionResponse.toRecipeInstructionEntity(remoteId: String) =
    RecipeInstructionEntity(
        recipeId = remoteId,
        title = title,
        text = text
    )

fun GetRecipeSummaryResponseV1.recipeEntity() = RecipeSummaryEntity(
    remoteId = remoteId,
    name = name,
    slug = slug,
    image = image,
    description = description,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
)

fun VersionResponse.toVersionInfo() = VersionInfo(version)

fun VersionResponseV1.toVersionInfo() = VersionInfo(version)

fun AddRecipeDraft.toAddRecipeRequest() = AddRecipeRequest(
    name = recipeName,
    description = recipeDescription,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredients.map { AddRecipeIngredient(note = it) },
    recipeInstructions = recipeInstructions.map { AddRecipeInstruction(text = it) },
    settings = AddRecipeSettings(
        public = isRecipePublic,
        disableComments = areCommentsDisabled,
    )
)

fun AddRecipeRequest.toDraft(): AddRecipeDraft = AddRecipeDraft(
    recipeName = name,
    recipeDescription = description,
    recipeYield = recipeYield,
    recipeInstructions = recipeInstructions.map { it.text },
    recipeIngredients = recipeIngredient.map { it.note },
    isRecipePublic = settings.public,
    areCommentsDisabled = settings.disableComments,
)
