package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeIngredientInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeInstructionInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.*
import gq.kirmanak.mealient.datasource.v1.models.*
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

fun FullRecipeInfo.toRecipeEntity() = RecipeEntity(
    remoteId = remoteId,
    recipeYield = recipeYield
)

fun RecipeIngredientInfo.toRecipeIngredientEntity(remoteId: String) = RecipeIngredientEntity(
    recipeId = remoteId,
    title = title,
    note = note,
    unit = unit,
    food = food,
    disableAmount = disableAmount,
    quantity = quantity
)

fun RecipeInstructionInfo.toRecipeInstructionEntity(remoteId: String) = RecipeInstructionEntity(
    recipeId = remoteId,
    title = title,
    text = text
)

fun GetRecipeSummaryResponse.toRecipeSummaryInfo() = RecipeSummaryInfo(
    remoteId = remoteId.toString(),
    name = name,
    slug = slug,
    image = image,
    description = description,
    recipeCategories = recipeCategories,
    tags = tags,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = slug,
)

fun GetRecipeSummaryResponseV1.toRecipeSummaryInfo() = RecipeSummaryInfo(
    remoteId = remoteId,
    name = name,
    slug = slug,
    image = image,
    description = description,
    recipeCategories = recipeCategories,
    tags = tags,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = remoteId,
)

fun RecipeSummaryInfo.recipeEntity() = RecipeSummaryEntity(
    remoteId = remoteId,
    name = name,
    slug = slug,
    image = image,
    description = description,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    imageId = imageId,
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

fun GetRecipeResponse.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId.toString(),
    name = name,
    slug = slug,
    image = image,
    description = description,
    recipeCategories = recipeCategories,
    tags = tags,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() }
)

fun GetRecipeIngredientResponse.toRecipeIngredientInfo() = RecipeIngredientInfo(
    title = title,
    note = note,
    unit = unit,
    food = food,
    disableAmount = disableAmount,
    quantity = quantity
)

fun GetRecipeInstructionResponse.toRecipeInstructionInfo() = RecipeInstructionInfo(
    title = title,
    text = text
)

fun GetRecipeResponseV1.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId,
    name = name,
    slug = slug,
    image = image,
    description = description,
    recipeCategories = recipeCategories,
    tags = tags,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() }
)

fun GetRecipeIngredientResponseV1.toRecipeIngredientInfo() = RecipeIngredientInfo(
    title = title,
    note = note,
    unit = unit,
    food = food,
    disableAmount = disableAmount,
    quantity = quantity
)

fun GetRecipeInstructionResponseV1.toRecipeInstructionInfo() = RecipeInstructionInfo(
    title = title,
    text = text
)
