package gq.kirmanak.mealient.datastore.recipe

import androidx.datastore.core.DataStore
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddRecipeStorageImpl @Inject constructor(
    private val dataStore: DataStore<AddRecipeInput>,
    private val logger: Logger,
) : AddRecipeStorage {

    override val updates: Flow<AddRecipeDraft>
        get() = dataStore.data.map {
            AddRecipeDraft(
                recipeName = it.recipeName,
                recipeDescription = it.recipeDescription,
                recipeYield = it.recipeYield,
                recipeInstructions = it.recipeInstructionsList,
                recipeIngredients = it.recipeIngredientsList,
                isRecipePublic = it.isRecipePublic,
                areCommentsDisabled = it.areCommentsDisabled,
            )
        }

    override suspend fun save(addRecipeDraft: AddRecipeDraft) {
        logger.v { "save() called with: addRecipeDraft = $addRecipeDraft" }
        val input = AddRecipeInput.newBuilder()
            .setRecipeName(addRecipeDraft.recipeName)
            .setRecipeDescription(addRecipeDraft.recipeDescription)
            .setRecipeYield(addRecipeDraft.recipeYield)
            .setIsRecipePublic(addRecipeDraft.isRecipePublic)
            .setAreCommentsDisabled(addRecipeDraft.areCommentsDisabled)
            .addAllRecipeIngredients(addRecipeDraft.recipeIngredients)
            .addAllRecipeInstructions(addRecipeDraft.recipeInstructions)
            .build()
        dataStore.updateData { input }
    }

    override suspend fun clear() {
        logger.v { "clear() called" }
        dataStore.updateData { AddRecipeInput.getDefaultInstance() }
    }
}