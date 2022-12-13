package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class RecipeRepoImpl @Inject constructor(
    private val mediator: RecipesRemoteMediator,
    private val storage: RecipeStorage,
    private val pagingSourceFactory: RecipePagingSourceFactory,
    private val dataSource: RecipeDataSource,
    private val logger: Logger,
) : RecipeRepo {

    override fun createPager(): Pager<Int, RecipeSummaryEntity> {
        logger.v { "createPager() called" }
        val pagingConfig = PagingConfig(
            pageSize = LOAD_PAGE_SIZE,
            enablePlaceholders = true,
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
            storage.saveRecipeInfo(dataSource.requestRecipeInfo(recipeSlug))
        }.onFailure {
            logger.e(it) { "loadRecipeInfo: can't update full recipe info" }
        }
    }

    override suspend fun loadRecipeInfo(recipeId: String): FullRecipeEntity? {
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

    override suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean) {
        logger.v { "updateIsRecipeFavorite() called with: recipeSlug = $recipeSlug, isFavorite = $isFavorite" }
        runCatchingExceptCancel {
            dataSource.updateIsRecipeFavorite(recipeSlug, isFavorite)
            mediator.onFavoritesChange()
        }.onFailure {
            logger.e(it) { "Can't update recipe's is favorite status" }
        }
    }

    companion object {
        private const val LOAD_PAGE_SIZE = 50
        private const val INITIAL_LOAD_PAGE_SIZE = LOAD_PAGE_SIZE * 3
    }
}