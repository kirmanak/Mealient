package gq.kirmanak.mealient.model_mapper

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredient
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstruction
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettings
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.CreateRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeIngredientResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeInstructionResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

interface ModelMapper {

    fun toRecipeEntity(getRecipeResponse: GetRecipeResponse): RecipeEntity

    fun toRecipeIngredientEntity(
        ingredientResponse: GetRecipeIngredientResponse, remoteId: String
    ): RecipeIngredientEntity

    fun toRecipeInstructionEntity(
        instructionResponse: GetRecipeInstructionResponse, remoteId: String
    ): RecipeInstructionEntity

    fun toRecipeSummaryEntity(
        recipeSummaryInfo: GetRecipeSummaryResponse, isFavorite: Boolean
    ): RecipeSummaryEntity

    fun toAddRecipeInfo(addRecipeDraft: AddRecipeDraft): AddRecipeInfo

    fun toDraft(addRecipeInfo: AddRecipeInfo): AddRecipeDraft

    fun toCreateRequest(addRecipeInfo: AddRecipeInfo): CreateRecipeRequest

    fun toUpdateRequest(addRecipeInfo: AddRecipeInfo): UpdateRecipeRequest

    fun toSettings(addRecipeSettingsInfo: AddRecipeSettingsInfo): AddRecipeSettings

    fun toIngredient(addRecipeIngredientInfo: AddRecipeIngredientInfo): AddRecipeIngredient

    fun toInstruction(addRecipeInstructionInfo: AddRecipeInstructionInfo): AddRecipeInstruction

}