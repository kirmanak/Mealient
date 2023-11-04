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
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLRequest
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
import java.util.UUID
import javax.inject.Inject

class ModelMapperImpl @Inject constructor() : ModelMapper {


    override fun toRecipeEntity(fullRecipeInfo: FullRecipeInfo) = RecipeEntity(
        remoteId = fullRecipeInfo.remoteId,
        recipeYield = fullRecipeInfo.recipeYield,
        disableAmounts = fullRecipeInfo.settings.disableAmounts,
    )

    override fun toRecipeIngredientEntity(
        recipeIngredientInfo: RecipeIngredientInfo, remoteId: String
    ) = RecipeIngredientEntity(
        recipeId = remoteId,
        note = recipeIngredientInfo.note,
        unit = recipeIngredientInfo.unit,
        food = recipeIngredientInfo.food,
        quantity = recipeIngredientInfo.quantity,
        title = recipeIngredientInfo.title,
    )

    override fun toRecipeInstructionEntity(
        recipeInstructionInfo: RecipeInstructionInfo, remoteId: String
    ) = RecipeInstructionEntity(
        recipeId = remoteId, text = recipeInstructionInfo.text
    )

    override fun toRecipeSummaryEntity(
        recipeSummaryInfo: GetRecipeSummaryResponse,
        isFavorite: Boolean
    ) =
        RecipeSummaryEntity(
            remoteId = recipeSummaryInfo.remoteId,
            name = recipeSummaryInfo.name,
            slug = recipeSummaryInfo.slug,
            description = recipeSummaryInfo.description,
            dateAdded = recipeSummaryInfo.dateAdded,
            dateUpdated = recipeSummaryInfo.dateUpdated,
            imageId = recipeSummaryInfo.remoteId,
            isFavorite = isFavorite,
        )

    override fun toAddRecipeInfo(addRecipeDraft: AddRecipeDraft) = AddRecipeInfo(
        name = addRecipeDraft.recipeName,
        description = addRecipeDraft.recipeDescription,
        recipeYield = addRecipeDraft.recipeYield,
        recipeIngredient = addRecipeDraft.recipeIngredients.map { AddRecipeIngredientInfo(note = it) },
        recipeInstructions = addRecipeDraft.recipeInstructions.map { AddRecipeInstructionInfo(text = it) },
        settings = AddRecipeSettingsInfo(
            public = addRecipeDraft.isRecipePublic,
            disableComments = addRecipeDraft.areCommentsDisabled,
        )
    )

    override fun toDraft(addRecipeInfo: AddRecipeInfo): AddRecipeDraft = AddRecipeDraft(
        recipeName = addRecipeInfo.name,
        recipeDescription = addRecipeInfo.description,
        recipeYield = addRecipeInfo.recipeYield,
        recipeInstructions = addRecipeInfo.recipeInstructions.map { it.text },
        recipeIngredients = addRecipeInfo.recipeIngredient.map { it.note },
        isRecipePublic = addRecipeInfo.settings.public,
        areCommentsDisabled = addRecipeInfo.settings.disableComments,
    )


    override fun toFullRecipeInfo(getRecipeResponse: GetRecipeResponse) = FullRecipeInfo(
        remoteId = getRecipeResponse.remoteId,
        name = getRecipeResponse.name,
        recipeYield = getRecipeResponse.recipeYield,
        recipeIngredients = getRecipeResponse.recipeIngredients.map { toRecipeIngredientInfo(it) },
        recipeInstructions = getRecipeResponse.recipeInstructions.map { toRecipeInstructionInfo(it) },
        settings = toRecipeSettingsInfo(getRecipeResponse.settings),
    )

    override fun toRecipeSettingsInfo(getRecipeSettingsResponse: GetRecipeSettingsResponse?) =
        RecipeSettingsInfo(
            disableAmounts = getRecipeSettingsResponse?.disableAmount ?: true,
        )

    override fun toRecipeIngredientInfo(getRecipeIngredientResponse: GetRecipeIngredientResponse) =
        RecipeIngredientInfo(
            note = getRecipeIngredientResponse.note,
            unit = getRecipeIngredientResponse.unit?.name,
            food = getRecipeIngredientResponse.food?.name,
            quantity = getRecipeIngredientResponse.quantity,
            title = getRecipeIngredientResponse.title,
        )

    override fun toRecipeInstructionInfo(getRecipeInstructionResponse: GetRecipeInstructionResponse) =
        RecipeInstructionInfo(
            text = getRecipeInstructionResponse.text
        )

    override fun toCreateRequest(addRecipeInfo: AddRecipeInfo) = CreateRecipeRequest(
        name = addRecipeInfo.name,
    )

    override fun toUpdateRequest(addRecipeInfo: AddRecipeInfo) = UpdateRecipeRequest(
        description = addRecipeInfo.description,
        recipeYield = addRecipeInfo.recipeYield,
        recipeIngredient = addRecipeInfo.recipeIngredient.map { toIngredient(it) },
        recipeInstructions = addRecipeInfo.recipeInstructions.map { toInstruction(it) },
        settings = toSettings(addRecipeInfo.settings),
    )

    override fun toSettings(addRecipeSettingsInfo: AddRecipeSettingsInfo) = AddRecipeSettings(
        disableComments = addRecipeSettingsInfo.disableComments,
        public = addRecipeSettingsInfo.public,
    )

    override fun toIngredient(addRecipeIngredientInfo: AddRecipeIngredientInfo) =
        AddRecipeIngredient(
            id = UUID.randomUUID().toString(),
            note = addRecipeIngredientInfo.note,
        )

    override fun toInstruction(addRecipeInstructionInfo: AddRecipeInstructionInfo) =
        AddRecipeInstruction(
            id = UUID.randomUUID().toString(),
            text = addRecipeInstructionInfo.text,
            ingredientReferences = emptyList(),
        )

    override fun toRequest(parseRecipeURLInfo: ParseRecipeURLInfo) = ParseRecipeURLRequest(
        url = parseRecipeURLInfo.url,
        includeTags = parseRecipeURLInfo.includeTags,
    )

    override fun toFullShoppingListInfo(getShoppingListResponse: GetShoppingListResponse): FullShoppingListInfo {
        val recipes = getShoppingListResponse.recipeReferences.groupBy { it.recipeId }
        return FullShoppingListInfo(
            id = getShoppingListResponse.id,
            name = getShoppingListResponse.name,
            items = getShoppingListResponse.listItems.map { toShoppingListItemInfo(it, recipes) },
        )
    }

    override fun toShoppingListItemInfo(
        getShoppingListItemResponse: GetShoppingListItemResponse,
        recipes: Map<String, List<GetShoppingListItemRecipeReferenceFullResponse>>
    ): ShoppingListItemInfo = ShoppingListItemInfo(
        shoppingListId = getShoppingListItemResponse.shoppingListId,
        id = getShoppingListItemResponse.id,
        checked = getShoppingListItemResponse.checked,
        position = getShoppingListItemResponse.position,
        isFood = getShoppingListItemResponse.isFood,
        note = getShoppingListItemResponse.note,
        quantity = getShoppingListItemResponse.quantity,
        unit = getShoppingListItemResponse.unit,
        food = getShoppingListItemResponse.food,
        recipeReferences = getShoppingListItemResponse.recipeReferences.map { it.recipeId }
            .mapNotNull { recipes[it] }.flatten().map { toShoppingListItemRecipeReferenceInfo(it) },
    )

    override fun toShoppingListItemRecipeReferenceInfo(
        getShoppingListItemRecipeReferenceFullResponse: GetShoppingListItemRecipeReferenceFullResponse
    ) = ShoppingListItemRecipeReferenceInfo(
        recipeId = getShoppingListItemRecipeReferenceFullResponse.recipeId,
        recipeQuantity = getShoppingListItemRecipeReferenceFullResponse.recipeQuantity,
        id = getShoppingListItemRecipeReferenceFullResponse.id,
        shoppingListId = getShoppingListItemRecipeReferenceFullResponse.shoppingListId,
        recipe = toFullRecipeInfo(getShoppingListItemRecipeReferenceFullResponse.recipe),
    )


}