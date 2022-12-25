package gq.kirmanak.mealient.shopping_lists.models

import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo

internal fun ShoppingListsInfo.toShoppingListEntities() = items.map { it.toShoppingListEntity() }

private fun ShoppingListInfo.toShoppingListEntity() = ShoppingListEntity(
    remoteId = id,
    name = name,
)