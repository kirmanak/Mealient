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
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.GetRecipeIngredientResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeInstructionResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSettingsResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemRecipeReferenceFullResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLRequest
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft

interface ModelMapper {

    fun toRecipeEntity(fullRecipeInfo: FullRecipeInfo): RecipeEntity

    fun toRecipeIngredientEntity(
        recipeIngredientInfo: RecipeIngredientInfo, remoteId: String
    ): RecipeIngredientEntity

    fun toRecipeInstructionEntity(
        recipeInstructionInfo: RecipeInstructionInfo, remoteId: String
    ): RecipeInstructionEntity

    fun toRecipeSummaryEntity(
        recipeSummaryInfo: RecipeSummaryInfo, isFavorite: Boolean
    ): RecipeSummaryEntity

    fun toAddRecipeInfo(addRecipeDraft: AddRecipeDraft): AddRecipeInfo

    fun toDraft(addRecipeInfo: AddRecipeInfo): AddRecipeDraft

    fun toFullRecipeInfo(getRecipeResponse: GetRecipeResponse): FullRecipeInfo

    fun toRecipeSettingsInfo(getRecipeSettingsResponse: GetRecipeSettingsResponse?): RecipeSettingsInfo

    fun toRecipeIngredientInfo(getRecipeIngredientResponse: GetRecipeIngredientResponse): RecipeIngredientInfo

    fun toRecipeInstructionInfo(getRecipeInstructionResponse: GetRecipeInstructionResponse): RecipeInstructionInfo

    fun toCreateRequest(addRecipeInfo: AddRecipeInfo): CreateRecipeRequest

    fun toUpdateRequest(addRecipeInfo: AddRecipeInfo): UpdateRecipeRequest

    fun toSettings(addRecipeSettingsInfo: AddRecipeSettingsInfo): AddRecipeSettings

    fun toIngredient(addRecipeIngredientInfo: AddRecipeIngredientInfo): AddRecipeIngredient

    fun toInstruction(addRecipeInstructionInfo: AddRecipeInstructionInfo): AddRecipeInstruction

    fun toRequest(parseRecipeURLInfo: ParseRecipeURLInfo): ParseRecipeURLRequest

    fun toFullShoppingListInfo(getShoppingListResponse: GetShoppingListResponse): FullShoppingListInfo

    fun toShoppingListItemInfo(
        getShoppingListItemResponse: GetShoppingListItemResponse,
        recipes: Map<String, List<GetShoppingListItemRecipeReferenceFullResponse>>
    ): ShoppingListItemInfo

    fun toShoppingListItemRecipeReferenceInfo(
        getShoppingListItemRecipeReferenceFullResponse: GetShoppingListItemRecipeReferenceFullResponse
    ): ShoppingListItemRecipeReferenceInfo

    fun toShoppingListsInfo(getShoppingListsResponse: GetShoppingListsResponse): ShoppingListsInfo

    fun toShoppingListInfo(getShoppingListsSummaryResponse: GetShoppingListsSummaryResponse): ShoppingListInfo

    fun toRecipeSummaryInfo(getRecipeSummaryResponse: GetRecipeSummaryResponse): RecipeSummaryInfo

}