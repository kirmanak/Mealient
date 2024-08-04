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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import javax.inject.Inject

class ModelMapperImpl @Inject constructor() : ModelMapper {


    override fun toRecipeEntity(getRecipeResponse: GetRecipeResponse) = RecipeEntity(
        remoteId = getRecipeResponse.remoteId,
        recipeYield = getRecipeResponse.recipeYield,
        disableAmounts = getRecipeResponse.settings?.disableAmount ?: true,
    )

    override fun toRecipeIngredientEntity(
        ingredientResponse: GetRecipeIngredientResponse,
        recipeId: String
    ) = RecipeIngredientEntity(
        id = ingredientResponse.referenceId,
        recipeId = recipeId,
        note = ingredientResponse.note,
        food = ingredientResponse.food?.name,
        unit = ingredientResponse.unit?.name,
        quantity = ingredientResponse.quantity,
        display = ingredientResponse.display,
        title = ingredientResponse.title,
        isFood = ingredientResponse.isFood,
        disableAmount = ingredientResponse.disableAmount,
    )

    override fun toRecipeInstructionEntity(
        instructionResponse: GetRecipeInstructionResponse,
        recipeId: String
    ) = RecipeInstructionEntity(
        id = instructionResponse.id,
        recipeId = recipeId,
        text = instructionResponse.text,
        title = instructionResponse.title,
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
            dateUpdated = recipeSummaryInfo.dateUpdated.toLocalDateTime(TimeZone.UTC),
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


}