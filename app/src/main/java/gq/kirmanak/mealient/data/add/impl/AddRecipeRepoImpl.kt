package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.model_mapper.ModelMapper
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
    private val modelMapper: ModelMapper,
) : AddRecipeRepo {

    override val addRecipeRequestFlow: Flow<AddRecipeInfo>
        get() = addRecipeStorage.updates.map { modelMapper.toAddRecipeInfo(it) }

    override suspend fun preserve(recipe: AddRecipeInfo) {
        logger.v { "preserveRecipe() called with: recipe = $recipe" }
        addRecipeStorage.save(modelMapper.toDraft(recipe))
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