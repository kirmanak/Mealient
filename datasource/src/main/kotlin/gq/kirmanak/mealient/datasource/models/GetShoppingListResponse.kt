package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListResponse(
    @SerialName("id") val id: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("name") val name: String = "",
    @SerialName("listItems") val listItems: List<GetShoppingListItemResponse> = emptyList(),
    @SerialName("recipeReferences") val recipeReferences: List<GetShoppingListItemRecipeReferenceFullResponse>,
)

@Serializable
data class GetShoppingListItemResponse(
    @SerialName("shoppingListId") val shoppingListId: String,
    @SerialName("id") val id: String,
    @SerialName("checked") val checked: Boolean = false,
    @SerialName("position") val position: Int = 0,
    @SerialName("isFood") val isFood: Boolean = false,
    @SerialName("note") val note: String = "",
    @SerialName("quantity") val quantity: Double = 0.0,
    @SerialName("unit") val unit: GetRecipeIngredientUnitResponse? = null,
    @SerialName("food") val food: GetRecipeIngredientFoodResponse? = null,
    @SerialName("recipeReferences") val recipeReferences: List<GetShoppingListItemRecipeReferenceResponse> = emptyList(),
)

@Serializable
data class GetShoppingListItemRecipeReferenceResponse(
    @SerialName("recipeId") val recipeId: String,
    @SerialName("recipeQuantity") val recipeQuantity: Double = 0.0
)

@Serializable
data class GetShoppingListItemRecipeReferenceFullResponse(
    @SerialName("id") val id: String,
    @SerialName("shoppingListId") val shoppingListId: String,
    @SerialName("recipeId") val recipeId: String,
    @SerialName("recipeQuantity") val recipeQuantity: Double = 0.0,
    @SerialName("recipe") val recipe: GetRecipeResponse,
)
