package gq.kirmanak.mealient.datasource.models

data class FullShoppingListInfo(
    val id: String,
    val name: String,
    val items: List<ShoppingListItemInfo>,
)

data class ShoppingListItemInfo(
    val shoppingListId: String,
    val id: String,
    val checked: Boolean,
    val position: Int,
    val isFood: Boolean,
    val note: String,
    val quantity: Double,
    val unit: GetUnitResponse?,
    val food: GetFoodResponse?,
    val recipeReferences: List<ShoppingListItemRecipeReferenceInfo>,
)

data class ShoppingListItemRecipeReferenceInfo(
    val recipeId: String,
    val recipeQuantity: Double,
    val id: String,
    val shoppingListId: String,
    val recipe: FullRecipeInfo,
)
