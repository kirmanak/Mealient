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
import gq.kirmanak.mealient.datasource.v0.models.*
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

fun GetRecipeSummaryResponseV0.toRecipeSummaryInfo() = RecipeSummaryInfo(
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

fun VersionResponseV0.toVersionInfo() = VersionInfo(version)

fun VersionResponseV1.toVersionInfo() = VersionInfo(version)

fun AddRecipeDraft.toAddRecipeRequest() = AddRecipeRequestV0(
    name = recipeName,
    description = recipeDescription,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredients.map { AddRecipeIngredientV0(note = it) },
    recipeInstructions = recipeInstructions.map { AddRecipeInstructionV0(text = it) },
    settings = AddRecipeSettingsV0(
        public = isRecipePublic,
        disableComments = areCommentsDisabled,
    )
)

fun AddRecipeRequestV0.toDraft(): AddRecipeDraft = AddRecipeDraft(
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

fun GetRecipeIngredientResponseV0.toRecipeIngredientInfo() = RecipeIngredientInfo(
    title = title,
    note = note,
    unit = unit,
    food = food,
    disableAmount = disableAmount,
    quantity = quantity
)

fun GetRecipeInstructionResponseV0.toRecipeInstructionInfo() = RecipeInstructionInfo(
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
