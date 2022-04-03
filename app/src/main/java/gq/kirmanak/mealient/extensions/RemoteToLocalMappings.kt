package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.baseurl.VersionResponse
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeIngredientResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeInstructionResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse

fun GetRecipeResponse.toRecipeEntity() = RecipeEntity(
    remoteId = remoteId,
    recipeYield = recipeYield
)

fun GetRecipeIngredientResponse.toRecipeIngredientEntity(remoteId: Long) =
    RecipeIngredientEntity(
        recipeId = remoteId,
        title = title,
        note = note,
        unit = unit,
        food = food,
        disableAmount = disableAmount,
        quantity = quantity
    )

fun GetRecipeInstructionResponse.toRecipeInstructionEntity(remoteId: Long) =
    RecipeInstructionEntity(
        recipeId = remoteId,
        title = title,
        text = text
    )

fun GetRecipeSummaryResponse.recipeEntity() = RecipeSummaryEntity(
    remoteId = remoteId,
    name = name,
    slug = slug,
    image = image,
    description = description,
    rating = rating,
    dateAdded = dateAdded,
    dateUpdated = dateUpdated,
)

fun VersionResponse.versionInfo() = VersionInfo(production, version, demoStatus)