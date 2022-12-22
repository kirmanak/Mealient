package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsSummaryResponseV1

internal fun GetShoppingListsSummaryResponseV1.toShoppingListInfo() = ShoppingListInfo(
    name = name.orEmpty(),
    id = id,
)