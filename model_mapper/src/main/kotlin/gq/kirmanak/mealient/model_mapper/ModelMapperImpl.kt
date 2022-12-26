package gq.kirmanak.mealient.model_mapper

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
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
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelMapperImpl @Inject constructor() : ModelMapper {

    override fun toShoppingListEntities(
        shoppingListsInfo: ShoppingListsInfo
    ) = shoppingListsInfo.items.map {
        toShoppingListEntity(it)
    }

    override fun toShoppingListEntity(
        shoppingListInfo: ShoppingListInfo
    ) = ShoppingListEntity(
        remoteId = shoppingListInfo.id,
        name = shoppingListInfo.name,
    )

    override fun toShoppingListItemEntity(
        shoppingListItemInfo: ShoppingListItemInfo
    ) = ShoppingListItemEntity(
        shoppingListId = shoppingListItemInfo.shoppingListId,
        remoteId = shoppingListItemInfo.id,
        checked = shoppingListItemInfo.checked,
        position = shoppingListItemInfo.position,
        isFood = shoppingListItemInfo.isFood,
        note = shoppingListItemInfo.note,
        quantity = shoppingListItemInfo.quantity,
        unit = shoppingListItemInfo.unit,
        food = shoppingListItemInfo.food,
    )

    override fun toShoppingListItemRecipeReferenceEntity(
        shoppingListItemRecipeReferenceInfo: ShoppingListItemRecipeReferenceInfo,
        shoppingListItemId: String
    ) = ShoppingListItemRecipeReferenceEntity(
        recipeId = shoppingListItemRecipeReferenceInfo.recipeId,
        quantity = shoppingListItemRecipeReferenceInfo.recipeQuantity,
        shoppingListItemId = shoppingListItemId,
    )


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

    override fun toRecipeSummaryEntity(recipeSummaryInfo: RecipeSummaryInfo, isFavorite: Boolean) =
        RecipeSummaryEntity(
            remoteId = recipeSummaryInfo.remoteId,
            name = recipeSummaryInfo.name,
            slug = recipeSummaryInfo.slug,
            description = recipeSummaryInfo.description,
            dateAdded = recipeSummaryInfo.dateAdded,
            dateUpdated = recipeSummaryInfo.dateUpdated,
            imageId = recipeSummaryInfo.imageId,
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


    override fun toVersionInfo(versionResponseV1: VersionResponseV1) =
        VersionInfo(versionResponseV1.version)

    override fun toFullRecipeInfo(getRecipeResponseV1: GetRecipeResponseV1) = FullRecipeInfo(
        remoteId = getRecipeResponseV1.remoteId,
        name = getRecipeResponseV1.name,
        recipeYield = getRecipeResponseV1.recipeYield,
        recipeIngredients = getRecipeResponseV1.recipeIngredients.map { toRecipeIngredientInfo(it) },
        recipeInstructions = getRecipeResponseV1.recipeInstructions.map { toRecipeInstructionInfo(it) },
        settings = toRecipeSettingsInfo(getRecipeResponseV1.settings),
    )

    override fun toRecipeSettingsInfo(getRecipeSettingsResponseV1: GetRecipeSettingsResponseV1?) =
        RecipeSettingsInfo(
            disableAmounts = getRecipeSettingsResponseV1?.disableAmount ?: true,
        )

    override fun toRecipeIngredientInfo(getRecipeIngredientResponseV1: GetRecipeIngredientResponseV1) =
        RecipeIngredientInfo(
            note = getRecipeIngredientResponseV1.note,
            unit = getRecipeIngredientResponseV1.unit?.name,
            food = getRecipeIngredientResponseV1.food?.name,
            quantity = getRecipeIngredientResponseV1.quantity,
            title = getRecipeIngredientResponseV1.title,
        )

    override fun toRecipeInstructionInfo(getRecipeInstructionResponseV1: GetRecipeInstructionResponseV1) =
        RecipeInstructionInfo(
            text = getRecipeInstructionResponseV1.text
        )

    override fun toV1CreateRequest(addRecipeInfo: AddRecipeInfo) = CreateRecipeRequestV1(
        name = addRecipeInfo.name,
    )

    override fun toV1UpdateRequest(addRecipeInfo: AddRecipeInfo) = UpdateRecipeRequestV1(
        description = addRecipeInfo.description,
        recipeYield = addRecipeInfo.recipeYield,
        recipeIngredient = addRecipeInfo.recipeIngredient.map { toV1Ingredient(it) },
        recipeInstructions = addRecipeInfo.recipeInstructions.map { toV1Instruction(it) },
        settings = toV1Settings(addRecipeInfo.settings),
    )

    override fun toV1Settings(addRecipeSettingsInfo: AddRecipeSettingsInfo) = AddRecipeSettingsV1(
        disableComments = addRecipeSettingsInfo.disableComments,
        public = addRecipeSettingsInfo.public,
    )

    override fun toV1Ingredient(addRecipeIngredientInfo: AddRecipeIngredientInfo) =
        AddRecipeIngredientV1(
            id = UUID.randomUUID().toString(),
            note = addRecipeIngredientInfo.note,
        )

    override fun toV1Instruction(addRecipeInstructionInfo: AddRecipeInstructionInfo) =
        AddRecipeInstructionV1(
            id = UUID.randomUUID().toString(),
            text = addRecipeInstructionInfo.text,
            ingredientReferences = emptyList(),
        )

    override fun toV1Request(parseRecipeURLInfo: ParseRecipeURLInfo) = ParseRecipeURLRequestV1(
        url = parseRecipeURLInfo.url,
        includeTags = parseRecipeURLInfo.includeTags,
    )

    override fun toFullShoppingListInfo(getShoppingListResponseV1: GetShoppingListResponseV1): FullShoppingListInfo {
        val recipes = getShoppingListResponseV1.recipeReferences.groupBy { it.recipeId }
        return FullShoppingListInfo(
            id = getShoppingListResponseV1.id,
            name = getShoppingListResponseV1.name,
            items = getShoppingListResponseV1.listItems.map { toShoppingListItemInfo(it, recipes) },
        )
    }

    override fun toShoppingListItemInfo(
        getShoppingListItemResponseV1: GetShoppingListItemResponseV1,
        recipes: Map<String, List<GetShoppingListItemRecipeReferenceFullResponseV1>>
    ): ShoppingListItemInfo = ShoppingListItemInfo(
        shoppingListId = getShoppingListItemResponseV1.shoppingListId,
        id = getShoppingListItemResponseV1.id,
        checked = getShoppingListItemResponseV1.checked,
        position = getShoppingListItemResponseV1.position,
        isFood = getShoppingListItemResponseV1.isFood,
        note = getShoppingListItemResponseV1.note,
        quantity = getShoppingListItemResponseV1.quantity,
        unit = getShoppingListItemResponseV1.unit?.name.orEmpty(),
        food = getShoppingListItemResponseV1.food?.name.orEmpty(),
        recipeReferences = getShoppingListItemResponseV1.recipeReferences.map { it.recipeId }
            .mapNotNull { recipes[it] }.flatten().map { toShoppingListItemRecipeReferenceInfo(it) },
    )

    override fun toShoppingListItemRecipeReferenceInfo(
        getShoppingListItemRecipeReferenceFullResponseV1: GetShoppingListItemRecipeReferenceFullResponseV1
    ) = ShoppingListItemRecipeReferenceInfo(
        recipeId = getShoppingListItemRecipeReferenceFullResponseV1.recipeId,
        recipeQuantity = getShoppingListItemRecipeReferenceFullResponseV1.recipeQuantity,
        id = getShoppingListItemRecipeReferenceFullResponseV1.id,
        shoppingListId = getShoppingListItemRecipeReferenceFullResponseV1.shoppingListId,
        recipe = toFullRecipeInfo(getShoppingListItemRecipeReferenceFullResponseV1.recipe),
    )

    override fun toShoppingListsInfo(getShoppingListsResponseV1: GetShoppingListsResponseV1) =
        ShoppingListsInfo(
            page = getShoppingListsResponseV1.page,
            perPage = getShoppingListsResponseV1.perPage,
            totalPages = getShoppingListsResponseV1.totalPages,
            totalItems = getShoppingListsResponseV1.total,
            items = getShoppingListsResponseV1.items.map { toShoppingListInfo(it) },
        )

    override fun toShoppingListInfo(getShoppingListsSummaryResponseV1: GetShoppingListsSummaryResponseV1) =
        ShoppingListInfo(
            name = getShoppingListsSummaryResponseV1.name.orEmpty(),
            id = getShoppingListsSummaryResponseV1.id,
        )

    override fun toRecipeSummaryInfo(getRecipeSummaryResponseV1: GetRecipeSummaryResponseV1) =
        RecipeSummaryInfo(
            remoteId = getRecipeSummaryResponseV1.remoteId,
            name = getRecipeSummaryResponseV1.name,
            slug = getRecipeSummaryResponseV1.slug,
            description = getRecipeSummaryResponseV1.description,
            dateAdded = getRecipeSummaryResponseV1.dateAdded,
            dateUpdated = getRecipeSummaryResponseV1.dateUpdated,
            imageId = getRecipeSummaryResponseV1.remoteId,
        )


    override fun toRecipeSummaryInfo(getRecipeSummaryResponseV0: GetRecipeSummaryResponseV0) =
        RecipeSummaryInfo(
            remoteId = getRecipeSummaryResponseV0.remoteId.toString(),
            name = getRecipeSummaryResponseV0.name,
            slug = getRecipeSummaryResponseV0.slug,
            description = getRecipeSummaryResponseV0.description,
            dateAdded = getRecipeSummaryResponseV0.dateAdded,
            dateUpdated = getRecipeSummaryResponseV0.dateUpdated,
            imageId = getRecipeSummaryResponseV0.slug,
        )

    override fun toVersionInfo(versionResponseV0: VersionResponseV0) =
        VersionInfo(versionResponseV0.version)

    override fun toFullRecipeInfo(getRecipeResponseV0: GetRecipeResponseV0) = FullRecipeInfo(
        remoteId = getRecipeResponseV0.remoteId.toString(),
        name = getRecipeResponseV0.name,
        recipeYield = getRecipeResponseV0.recipeYield,
        recipeIngredients = getRecipeResponseV0.recipeIngredients.map { toRecipeIngredientInfo(it) },
        recipeInstructions = getRecipeResponseV0.recipeInstructions.map { toRecipeInstructionInfo(it) },
        settings = RecipeSettingsInfo(disableAmounts = true)
    )

    override fun toRecipeIngredientInfo(getRecipeIngredientResponseV0: GetRecipeIngredientResponseV0) =
        RecipeIngredientInfo(
            note = getRecipeIngredientResponseV0.note,
            unit = null,
            food = null,
            quantity = 1.0,
            title = null,
        )

    override fun toRecipeInstructionInfo(getRecipeInstructionResponseV0: GetRecipeInstructionResponseV0) =
        RecipeInstructionInfo(
            text = getRecipeInstructionResponseV0.text
        )

    override fun toV0Request(addRecipeInfo: AddRecipeInfo) = AddRecipeRequestV0(
        name = addRecipeInfo.name,
        description = addRecipeInfo.description,
        recipeYield = addRecipeInfo.recipeYield,
        recipeIngredient = addRecipeInfo.recipeIngredient.map { toV0Ingredient(it) },
        recipeInstructions = addRecipeInfo.recipeInstructions.map { toV0Instruction(it) },
        settings = toV0Settings(addRecipeInfo.settings),
    )

    override fun toV0Settings(addRecipeSettingsInfo: AddRecipeSettingsInfo) = AddRecipeSettingsV0(
        disableComments = addRecipeSettingsInfo.disableComments,
        public = addRecipeSettingsInfo.public,
    )

    override fun toV0Ingredient(addRecipeIngredientInfo: AddRecipeIngredientInfo) =
        AddRecipeIngredientV0(
            note = addRecipeIngredientInfo.note,
        )

    override fun toV0Instruction(addRecipeInstructionInfo: AddRecipeInstructionInfo) =
        AddRecipeInstructionV0(
            text = addRecipeInstructionInfo.text,
        )

    override fun toV0Request(parseRecipeURLInfo: ParseRecipeURLInfo) = ParseRecipeURLRequestV0(
        url = parseRecipeURLInfo.url,
    )
}