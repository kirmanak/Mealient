package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo
import gq.kirmanak.mealient.datasource.models.VersionInfo
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeIngredientV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeInstructionV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeSettingsV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeIngredientResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeInstructionResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSettingsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListItemRecipeReferenceFullResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListItemResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import java.util.*

fun VersionResponseV1.toVersionInfo() = VersionInfo(version)

fun GetRecipeResponseV1.toFullRecipeInfo() = FullRecipeInfo(
    remoteId = remoteId,
    name = name,
    recipeYield = recipeYield,
    recipeIngredients = recipeIngredients.map { it.toRecipeIngredientInfo() },
    recipeInstructions = recipeInstructions.map { it.toRecipeInstructionInfo() },
    settings = settings.toRecipeSettingsInfo(),
)

private fun GetRecipeSettingsResponseV1?.toRecipeSettingsInfo() = RecipeSettingsInfo(
    disableAmounts = this?.disableAmount ?: true,
)

fun GetRecipeIngredientResponseV1.toRecipeIngredientInfo() = RecipeIngredientInfo(
    note = note,
    unit = unit?.name,
    food = food?.name,
    quantity = quantity,
    title = title,
)

fun GetRecipeInstructionResponseV1.toRecipeInstructionInfo() = RecipeInstructionInfo(
    text = text
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

fun GetShoppingListResponseV1.toFullShoppingListInfo(): FullShoppingListInfo {
    val recipes = recipeReferences.groupBy { it.recipeId }
    return FullShoppingListInfo(
        id = id,
        name = name,
        items = listItems.map { it.toShoppingListItemInfo(recipes) },
    )
}

private fun GetShoppingListItemResponseV1.toShoppingListItemInfo(
    recipes: Map<String, List<GetShoppingListItemRecipeReferenceFullResponseV1>>
): ShoppingListItemInfo = ShoppingListItemInfo(
    shoppingListId = shoppingListId,
    id = id,
    checked = checked,
    position = position,
    isFood = isFood,
    note = note,
    quantity = quantity,
    unit = unit?.name.orEmpty(),
    food = food?.name.orEmpty(),
    recipeReferences = recipeReferences
        .map { it.recipeId }
        .mapNotNull { recipes[it] }
        .flatten()
        .map { it.toShoppingListItemRecipeReferenceInfo() },
)

private fun GetShoppingListItemRecipeReferenceFullResponseV1.toShoppingListItemRecipeReferenceInfo() =
    ShoppingListItemRecipeReferenceInfo(
        recipeId = recipeId,
        recipeQuantity = recipeQuantity,
        id = id,
        shoppingListId = shoppingListId,
        recipe = recipe.toFullRecipeInfo(),
    )

fun GetShoppingListsResponseV1.toShoppingListsInfo() = ShoppingListsInfo(
    page = page,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = total,
    items = items.map { it.toShoppingListInfo() },
)

fun GetShoppingListsSummaryResponseV1.toShoppingListInfo() = ShoppingListInfo(
    name = name.orEmpty(),
    id = id,
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

