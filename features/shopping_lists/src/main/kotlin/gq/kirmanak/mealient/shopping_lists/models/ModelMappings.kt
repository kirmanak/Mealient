package gq.kirmanak.mealient.shopping_lists.models

import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsSummaryResponseV1
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsInfo

internal fun GetShoppingListsResponseV1.toShoppingListsInfo() = ShoppingListsInfo(
    page = page,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = total,
    items = items.map { it.toShoppingListInfo() },
)

internal fun GetShoppingListsSummaryResponseV1.toShoppingListInfo() = ShoppingListInfo(
    name = name.orEmpty(),
    id = id,
)

internal fun ShoppingListsInfo.toShoppingListEntities() = items.map { it.toShoppingListEntity() }

private fun ShoppingListInfo.toShoppingListEntity() = ShoppingListEntity(
    remoteId = id,
    name = name,
)
