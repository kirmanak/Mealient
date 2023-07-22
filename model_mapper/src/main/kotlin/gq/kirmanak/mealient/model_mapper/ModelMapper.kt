package gq.kirmanak.mealient.model_mapper

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.FoodInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.NewShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo
import gq.kirmanak.mealient.datasource.models.UnitInfo
import gq.kirmanak.mealient.datasource.models.VersionInfo
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeIngredientV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeInstructionV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeSettingsV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeIngredientResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeInstructionResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeIngredientV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeInstructionV1
import gq.kirmanak.mealient.datasource.v1.models.AddRecipeSettingsV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateShoppingListItemRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetFoodsResponseV1
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
import gq.kirmanak.mealient.datasource.v1.models.GetUnitsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
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

    fun toVersionInfo(versionResponseV1: VersionResponseV1): VersionInfo

    fun toFullRecipeInfo(getRecipeResponseV1: GetRecipeResponseV1): FullRecipeInfo

    fun toRecipeSettingsInfo(getRecipeSettingsResponseV1: GetRecipeSettingsResponseV1?): RecipeSettingsInfo

    fun toRecipeIngredientInfo(getRecipeIngredientResponseV1: GetRecipeIngredientResponseV1): RecipeIngredientInfo

    fun toRecipeInstructionInfo(getRecipeInstructionResponseV1: GetRecipeInstructionResponseV1): RecipeInstructionInfo

    fun toV1CreateRequest(addRecipeInfo: AddRecipeInfo): CreateRecipeRequestV1

    fun toV1UpdateRequest(addRecipeInfo: AddRecipeInfo): UpdateRecipeRequestV1

    fun toV1Settings(addRecipeSettingsInfo: AddRecipeSettingsInfo): AddRecipeSettingsV1

    fun toV1Ingredient(addRecipeIngredientInfo: AddRecipeIngredientInfo): AddRecipeIngredientV1

    fun toV1Instruction(addRecipeInstructionInfo: AddRecipeInstructionInfo): AddRecipeInstructionV1

    fun toV1Request(parseRecipeURLInfo: ParseRecipeURLInfo): ParseRecipeURLRequestV1

    fun toFullShoppingListInfo(getShoppingListResponseV1: GetShoppingListResponseV1): FullShoppingListInfo

    fun toShoppingListItemInfo(
        getShoppingListItemResponseV1: GetShoppingListItemResponseV1,
        recipes: Map<String, List<GetShoppingListItemRecipeReferenceFullResponseV1>>
    ): ShoppingListItemInfo

    fun toShoppingListItemRecipeReferenceInfo(
        getShoppingListItemRecipeReferenceFullResponseV1: GetShoppingListItemRecipeReferenceFullResponseV1
    ): ShoppingListItemRecipeReferenceInfo

    fun toShoppingListsInfo(getShoppingListsResponseV1: GetShoppingListsResponseV1): ShoppingListsInfo

    fun toShoppingListInfo(getShoppingListsSummaryResponseV1: GetShoppingListsSummaryResponseV1): ShoppingListInfo

    fun toRecipeSummaryInfo(getRecipeSummaryResponseV1: GetRecipeSummaryResponseV1): RecipeSummaryInfo

    fun toRecipeSummaryInfo(getRecipeSummaryResponseV0: GetRecipeSummaryResponseV0): RecipeSummaryInfo

    fun toVersionInfo(versionResponseV0: VersionResponseV0): VersionInfo

    fun toFullRecipeInfo(getRecipeResponseV0: GetRecipeResponseV0): FullRecipeInfo

    fun toRecipeIngredientInfo(getRecipeIngredientResponseV0: GetRecipeIngredientResponseV0): RecipeIngredientInfo

    fun toRecipeInstructionInfo(getRecipeInstructionResponseV0: GetRecipeInstructionResponseV0): RecipeInstructionInfo

    fun toV0Request(addRecipeInfo: AddRecipeInfo): AddRecipeRequestV0

    fun toV0Settings(addRecipeSettingsInfo: AddRecipeSettingsInfo): AddRecipeSettingsV0

    fun toV0Ingredient(addRecipeIngredientInfo: AddRecipeIngredientInfo): AddRecipeIngredientV0

    fun toV0Instruction(addRecipeInstructionInfo: AddRecipeInstructionInfo): AddRecipeInstructionV0

    fun toV0Request(parseRecipeURLInfo: ParseRecipeURLInfo): ParseRecipeURLRequestV0

    fun toFoodInfo(getFoodsResponseV1: GetFoodsResponseV1): List<FoodInfo>

    fun toUnitInfo(getUnitsResponseV1: GetUnitsResponseV1): List<UnitInfo>

    fun toV1CreateRequest(addRecipeInfo: NewShoppingListItemInfo): CreateShoppingListItemRequestV1
}