package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorage
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class ShoppingListsRepoImpl @Inject constructor(
    private val remoteMediator: ShoppingListsRemoteMediator,
    private val sourceFactory: ShoppingListsPagingSourceFactory,
    private val storage: ShoppingListsStorage,
    private val dataSource: ShoppingListsDataSource,
    private val logger: Logger,
    private val modelMapper: ModelMapper,
) : ShoppingListsRepo {

    override fun createPager(): Pager<Int, ShoppingListEntity> {
        logger.v { "createPager() called" }
        val pagingConfig = PagingConfig(
            pageSize = LOAD_PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_PAGE_SIZE,
        )
        return Pager(
            config = pagingConfig,
            remoteMediator = remoteMediator,
            pagingSourceFactory = sourceFactory,
        )
    }

    override fun getShoppingListWithItems(id: String): Flow<ShoppingListWithItems> {
        logger.v { "getShoppingList() called with: id = $id" }
        return storage.getShoppingListWithItems(id)
    }

    override suspend fun updateShoppingList(id: String) {
        logger.v { "updateShoppingList() called with: id = $id" }

        val response = dataSource.getShoppingList(id)

        val items: List<ShoppingListItemEntity> = response.items.map {
            modelMapper.toShoppingListItemEntity(it)
        }

        val references: List<ShoppingListItemRecipeReferenceEntity> = response.items.map { item ->
            item.recipeReferences.map {
                modelMapper.toShoppingListItemRecipeReferenceEntity(it, item.id)
            }
        }.flatten()

        val recipes: List<RecipeEntity> = response.items.map { item ->
            item.recipeReferences.map {
                modelMapper.toRecipeEntity(it.recipe)
            }
        }.flatten()

        val recipeIngredients: List<RecipeIngredientEntity> = response.items.map { item ->
            item.recipeReferences.map { reference ->
                reference.recipe.recipeIngredients.map {
                    modelMapper.toRecipeIngredientEntity(it, reference.recipeId)
                }
            }.flatten()
        }.flatten()

        storage.saveShoppingListItems(
            items,
            references,
            recipes,
            recipeIngredients,
        )
    }

    override suspend fun updateIsShoppingListItemChecked(id: String, isChecked: Boolean) {
        logger.v { "updateIsShoppingListItemChecked() called with: id = $id, isChecked = $isChecked" }
        dataSource.updateIsShoppingListItemChecked(id, isChecked)
    }

    companion object {
        private const val LOAD_PAGE_SIZE = 50
        private const val INITIAL_LOAD_PAGE_SIZE = LOAD_PAGE_SIZE * 3
    }
}