package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.RecipeStorage
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientToInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeWithSummaryAndIngredientsAndInstructions
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RecipeRepoImpl @Inject constructor(
    private val mediator: RecipesRemoteMediator,
    private val storage: RecipeStorage,
    private val pagingSourceFactory: RecipePagingSourceFactory,
    private val dataSource: RecipeDataSource,
    private val logger: Logger,
    private val modelMapper: ModelMapper,
) : RecipeRepo {

    override fun createPager(): Pager<Int, RecipeSummaryEntity> {
        logger.v { "createPager() called" }
        val pagingConfig = PagingConfig(
            pageSize = LOAD_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_PAGE_SIZE,
        )
        return Pager(
            config = pagingConfig,
            remoteMediator = mediator,
            pagingSourceFactory = pagingSourceFactory,
        )
    }

    override suspend fun clearLocalData() {
        logger.v { "clearLocalData() called" }
        storage.clearAllLocalData()
    }

    override suspend fun refreshRecipeInfo(recipeSlug: String): Result<Unit> {
        logger.v { "refreshRecipeInfo() called with: recipeSlug = $recipeSlug" }
        return runCatchingExceptCancel {
            val recipe = dataSource.requestRecipe(recipeSlug)
            val entity = modelMapper.toRecipeEntity(recipe)
            val settings = modelMapper.toRecipeSettingsEntity(recipe.settings)
            val ingredients = recipe.ingredients.map {
                modelMapper.toRecipeIngredientEntity(it, entity.remoteId)
            }
            val instructions = recipe.instructions.map {
                modelMapper.toRecipeInstructionEntity(it, entity.remoteId)
            }
            val ingredientToInstruction = recipe.instructions.flatMap { instruction ->
                instruction.ingredientReferences.map { ingredientReference ->
                    RecipeIngredientToInstructionEntity(
                        recipeId = entity.remoteId,
                        ingredientId = ingredientReference.referenceId,
                        instructionId = instruction.id,
                    )
                }
            }
            storage.saveRecipeInfo(entity, settings, ingredients, instructions, ingredientToInstruction)
        }.onFailure {
            logger.e(it) { "loadRecipeInfo: can't update full recipe info" }
        }
    }

    override suspend fun loadRecipeInfo(recipeId: String): RecipeWithSummaryAndIngredientsAndInstructions? {
        logger.v { "loadRecipeInfo() called with: recipeId = $recipeId" }
        val recipeInfo = storage.queryRecipeInfo(recipeId)
        logger.v { "loadRecipeInfo() returned: $recipeInfo" }
        return recipeInfo
    }

    override fun updateNameQuery(name: String?) {
        logger.v { "updateNameQuery() called with: name = $name" }
        pagingSourceFactory.setQuery(name)
    }

    override suspend fun refreshRecipes() {
        logger.v { "refreshRecipes() called" }
        runCatchingExceptCancel {
            mediator.updateRecipes(0, INITIAL_LOAD_PAGE_SIZE)
        }.onFailure {
            logger.e(it) { "Can't refresh recipes" }
        }
    }

    override suspend fun updateIsRecipeFavorite(
        recipeSlug: String,
        isFavorite: Boolean,
    ): Result<Boolean> = runCatchingExceptCancel {
        logger.v { "updateIsRecipeFavorite() called with: recipeSlug = $recipeSlug, isFavorite = $isFavorite" }
        dataSource.updateIsRecipeFavorite(recipeSlug, isFavorite)
        val favorites = dataSource.getFavoriteRecipes()
        storage.updateFavoriteRecipes(favorites)
        pagingSourceFactory.invalidate()
        favorites.contains(recipeSlug)
    }.onFailure {
        logger.e(it) { "Can't update recipe's is favorite status" }
    }

    override suspend fun deleteRecipe(
        entity: RecipeSummaryEntity
    ): Result<Unit> = runCatchingExceptCancel {
        logger.v { "deleteRecipe() called with: entity = $entity" }
        dataSource.deleteRecipe(entity.slug)
        storage.deleteRecipe(entity)
        pagingSourceFactory.invalidate()
    }.onFailure {
        logger.e(it) { "Can't delete recipe" }
    }

    companion object {
        private const val LOAD_PAGE_SIZE = 50
        private const val INITIAL_LOAD_PAGE_SIZE = LOAD_PAGE_SIZE * 3
    }
}