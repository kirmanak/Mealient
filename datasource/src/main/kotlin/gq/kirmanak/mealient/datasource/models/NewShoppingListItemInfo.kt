package gq.kirmanak.mealient.datasource.models

data class NewShoppingListItemInfo(
    val shoppingListId: String,
    val isFood: Boolean,
    val note: String,
    val quantity: Double,
    val unit: UnitInfo?,
    val food: FoodInfo?,
    val position: Int,
)
