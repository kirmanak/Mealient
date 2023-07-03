package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorage
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsRepoImpl @Inject constructor(
    private val storage: ShoppingListsStorage,
    private val dataSource: ShoppingListsDataSource,
    private val logger: Logger,
    private val modelMapper: ModelMapper,
) : ShoppingListsRepo {

    override fun shoppingListWithItemsFlow(id: String): Flow<ShoppingListWithItems> {
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

    override suspend fun clearLocalData() {
        logger.v { "clearLocalData() called" }
        storage.clearLocalData()
    }

    override suspend fun getShoppingLists(): List<ShoppingListInfo> {
        logger.v { "getShoppingLists() called" }
        return dataSource.getAllShoppingLists()
    }

    override suspend fun getShoppingList(id: String): FullShoppingListInfo {
        logger.v { "getShoppingListItems() called with: id = $id" }
        return dataSource.getShoppingList(id)
    }
}