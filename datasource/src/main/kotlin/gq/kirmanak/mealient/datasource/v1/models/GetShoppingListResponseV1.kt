package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListResponseV1(
    @SerialName("id") val id: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("name") val name: String = "",
    @SerialName("listItems") val listItems: List<GetShoppingListItemResponseV1> = emptyList(),
    @SerialName("recipeReferences") val recipeReferences: List<GetShoppingListItemRecipeReferenceFullResponseV1>,
)

@Serializable
data class GetShoppingListItemResponseV1(
    @SerialName("shoppingListId") val shoppingListId: String,
    @SerialName("id") val id: String,
    @SerialName("checked") val checked: Boolean = false,
    @SerialName("position") val position: Int = 0,
    @SerialName("isFood") val isFood: Boolean = false,
    @SerialName("note") val note: String = "",
    @SerialName("quantity") val quantity: Double = 0.0,
    @SerialName("unit") val unit: GetRecipeIngredientUnitResponseV1? = null,
    @SerialName("food") val food: GetRecipeIngredientFoodResponseV1? = null,
    @SerialName("recipeReferences") val recipeReferences: List<GetShoppingListItemRecipeReferenceResponseV1> = emptyList(),
)

@Serializable
data class GetShoppingListItemRecipeReferenceResponseV1(
    @SerialName("recipeId") val recipeId: String,
    @SerialName("recipeQuantity") val recipeQuantity: Double = 0.0
)

@Serializable
data class GetShoppingListItemRecipeReferenceFullResponseV1(
    @SerialName("id") val id: String,
    @SerialName("shoppingListId") val shoppingListId: String,
    @SerialName("recipeId") val recipeId: String,
    @SerialName("recipeQuantity") val recipeQuantity: Double = 0.0,
    @SerialName("recipe") val recipe: GetRecipeResponseV1,
)
