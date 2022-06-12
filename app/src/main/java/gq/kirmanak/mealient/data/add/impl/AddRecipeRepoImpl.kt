package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.AddRecipeStorage
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeRepoImpl @Inject constructor(
    private val addRecipeDataSource: AddRecipeDataSource,
    private val addRecipeStorage: AddRecipeStorage,
) : AddRecipeRepo {

    override val addRecipeRequestFlow: Flow<AddRecipeRequest>
        get() = addRecipeStorage.updates

    override suspend fun preserve(recipe: AddRecipeRequest) {
        Timber.v("preserveRecipe() called with: recipe = $recipe")
        addRecipeStorage.save(recipe)
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