package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.extensions.toAddRecipeRequest
import gq.kirmanak.mealient.extensions.toDraft
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeRepoImpl @Inject constructor(
    private val addRecipeDataSource: AddRecipeDataSource,
    private val addRecipeStorage: AddRecipeStorage,
    private val logger: Logger,
) : AddRecipeRepo {

    override val addRecipeRequestFlow: Flow<AddRecipeRequestV0>
        get() = addRecipeStorage.updates.map { it.toAddRecipeRequest() }

    override suspend fun preserve(recipe: AddRecipeRequestV0) {
        logger.v { "preserveRecipe() called with: recipe = $recipe" }
        addRecipeStorage.save(recipe.toDraft())
    }

    override suspend fun clear() {
        logger.v { "clear() called" }
        addRecipeStorage.clear()
    }

    override suspend fun saveRecipe(): String {
        logger.v { "saveRecipe() called" }
        return addRecipeDataSource.addRecipe(addRecipeRequestFlow.first())
    }
}