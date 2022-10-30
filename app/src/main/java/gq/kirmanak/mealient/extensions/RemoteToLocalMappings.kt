package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.add.*
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

fun AddRecipeInfo.toV0Request() = AddRecipeRequestV0(
    name = name,
    description = description,
    image = image,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredient.map { it.toV0Ingredient() },
    recipeInstructions = recipeInstructions.map { it.toV0Instruction() },
    slug = slug,
    filePath = filePath,
    tags = tags,
    categories = categories,
    notes = notes.map { it.toV0Note() },
    extras = extras,
    assets = assets,
    settings = settings.toV0Settings(),
)

private fun AddRecipeSettingsInfo.toV0Settings() = AddRecipeSettingsV0(
    disableAmount = disableAmount,
    disableComments = disableComments,
    landscapeView = landscapeView,
    public = public,
    showAssets = showAssets,
    showNutrition = showNutrition,
)

private fun AddRecipeNoteInfo.toV0Note() = AddRecipeNoteV0(
    title = title,
    text = text,
)

private fun AddRecipeIngredientInfo.toV0Ingredient() = AddRecipeIngredientV0(
    disableAmount = disableAmount,
    food = food,
    note = note,
    quantity = quantity,
    title = title,
    unit = unit
)

private fun AddRecipeInstructionInfo.toV0Instruction() = AddRecipeInstructionV0(
    title = title,
    text = text,
)


fun AddRecipeInfo.toV1CreateRequest() = CreateRecipeRequestV1(
    name = name,
)

fun AddRecipeInfo.toV1UpdateRequest(slug: String) = UpdateRecipeRequestV1(
    name = name,
    description = description,
    image = image,
    recipeYield = recipeYield,
    recipeIngredient = recipeIngredient.map { it.toV1Ingredient() },
    recipeInstructions = recipeInstructions.map { it.toV1Instruction() },
    slug = slug,
    filePath = filePath,
    tags = tags,
    categories = categories,
    notes = notes.map { it.toV1Note() },
    extras = extras,
    assets = assets,
    settings = settings.toV1Settings(),
)

private fun AddRecipeSettingsInfo.toV1Settings() = AddRecipeSettingsV1(
    disableAmount = disableAmount,
    disableComments = disableComments,
    landscapeView = landscapeView,
    public = public,
    showAssets = showAssets,
    showNutrition = showNutrition,
)

private fun AddRecipeNoteInfo.toV1Note() = AddRecipeNoteV1(
    title = title,
    text = text,
)

private fun AddRecipeIngredientInfo.toV1Ingredient() = AddRecipeIngredientV1(
    disableAmount = disableAmount,
    food = food,
    note = note,
    quantity = quantity,
    title = title,
    unit = unit
)

private fun AddRecipeInstructionInfo.toV1Instruction() = AddRecipeInstructionV1(
    title = title,
    text = text,
)