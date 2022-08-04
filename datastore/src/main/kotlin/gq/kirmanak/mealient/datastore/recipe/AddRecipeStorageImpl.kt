package gq.kirmanak.mealient.datastore.recipe

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeStorageImpl @Inject constructor(
    private val dataStore: DataStore<AddRecipeInput>,
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

    override suspend fun save(addRecipeRequest: AddRecipeDraft) {
        val input = AddRecipeInput.newBuilder()
            .setRecipeName(addRecipeRequest.recipeName)
            .setRecipeDescription(addRecipeRequest.recipeDescription)
            .setRecipeYield(addRecipeRequest.recipeYield)
            .setIsRecipePublic(addRecipeRequest.isRecipePublic)
            .setAreCommentsDisabled(addRecipeRequest.areCommentsDisabled)
            .addAllRecipeIngredients(addRecipeRequest.recipeIngredients)
            .addAllRecipeInstructions(addRecipeRequest.recipeInstructions)
            .build()
        dataStore.updateData { input }
    }

    override suspend fun clear() {
        dataStore.updateData { AddRecipeInput.getDefaultInstance() }
    }
}