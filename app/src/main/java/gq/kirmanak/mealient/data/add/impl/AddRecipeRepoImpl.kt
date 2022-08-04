package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.models.AddRecipeIngredient
import gq.kirmanak.mealient.data.add.models.AddRecipeInstruction
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.data.add.models.AddRecipeSettings
import gq.kirmanak.mealient.datastore.recipe.AddRecipeDraft
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeRepoImpl @Inject constructor(
    private val addRecipeDataSource: AddRecipeDataSource,
    private val addRecipeStorage: AddRecipeStorage,
) : AddRecipeRepo {

    override val addRecipeRequestFlow: Flow<AddRecipeRequest>
        get() = addRecipeStorage.updates.map { it ->
            AddRecipeRequest(
                name = it.recipeName,
                description = it.recipeDescription,
                recipeYield = it.recipeYield,
                recipeIngredient = it.recipeIngredients.map { AddRecipeIngredient(note = it) },
                recipeInstructions = it.recipeInstructions.map { AddRecipeInstruction(text = it) },
                settings = AddRecipeSettings(
                    public = it.isRecipePublic,
                    disableComments = it.areCommentsDisabled,
                )
            )
        }

    override suspend fun preserve(recipe: AddRecipeRequest) {
        Timber.v("preserveRecipe() called with: recipe = $recipe")
        val input = AddRecipeDraft(
            recipeName = recipe.name,
            recipeDescription = recipe.description,
            recipeYield = recipe.recipeYield,
            recipeInstructions = recipe.recipeInstructions.map { it.text },
            recipeIngredients = recipe.recipeIngredient.map { it.note },
            isRecipePublic = recipe.settings.public,
            areCommentsDisabled = recipe.settings.disableComments,
        )
        addRecipeStorage.save(input)
    }

    override suspend fun clear() {
        Timber.v("clear() called")
        addRecipeStorage.clear()
    }

    override suspend fun saveRecipe(): String {
        Timber.v("saveRecipe() called")
        return addRecipeDataSource.addRecipe(addRecipeRequestFlow.first())
    }
}