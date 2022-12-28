package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ItemRecipeReferenceWithRecipe
import gq.kirmanak.mealient.database.shopping_lists.entity.RecipeWithIngredients
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemWithRecipes
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems

data class ShoppingListNavArgs(
    val shoppingListId: String,
)

@Destination(
    navArgsDelegate = ShoppingListNavArgs::class,
)
@Composable
fun ShoppingListScreen(
    shoppingListsViewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val screenState = shoppingListsViewModel.shoppingList.collectAsState()

    screenState.value?.let { shoppingList ->
        ShoppingListItems(shoppingList)
    } ?: Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Loading...", modifier = Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun ShoppingListItems(
    shoppingList: ShoppingListWithItems,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val nonChecked = shoppingList.shoppingListItems.filter { !it.item.checked }
        val checked = shoppingList.shoppingListItems.filter { it.item.checked }

        if (nonChecked.isNotEmpty()) {
            LazyColumn {
                items(nonChecked) {
                    ShoppingListItem(it)
                }
            }
        }

        if (checked.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "Checked"
            )

            LazyColumn {
                items(checked) {
                    ShoppingListItem(it)
                }
            }
        }

        if (checked.isEmpty() && nonChecked.isEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "No items in this list"
            )
        }
    }
}

@Composable
fun ShoppingListItem(
    shoppingListItem: ShoppingListItemWithRecipes,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Checkbox(
            checked = shoppingListItem.item.checked,
            onCheckedChange = { },
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = "${shoppingListItem.item.quantity} ${shoppingListItem.item.unit}",
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = shoppingListItem.item.food,
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = shoppingListItem.item.note,
        )

    }
}

@Composable
@Preview
fun PreviewShoppingListInfo() {
    val teaWithMilkRecipe = RecipeWithIngredients(
        recipe = RecipeEntity(
            remoteId = "1",
            recipeYield = "1 serving",
            disableAmounts = false,
        ),
        ingredients = listOf(
            RecipeIngredientEntity(
                localId = 1,
                recipeId = "1",
                note = "Tea bag",
                food = "",
                unit = "",
                quantity = 1.0,
                title = "",
            ),
            RecipeIngredientEntity(
                localId = 1,
                recipeId = "1",
                note = "",
                food = "Milk",
                unit = "ml",
                quantity = 500.0,
                title = "",
            ),
        ),
    )
    val shoppingList = ShoppingListWithItems(
        shoppingList = ShoppingListEntity(
            remoteId = "1",
            name = "Tea with milk",
        ),
        shoppingListItems = listOf(
            ShoppingListItemWithRecipes(
                item = ShoppingListItemEntity(
                    remoteId = "1",
                    shoppingListId = "1",
                    checked = false,
                    position = 0,
                    isFood = false,
                    note = "Black tea bags",
                    quantity = 30.0,
                    unit = "",
                    food = "",
                ),
                recipes = listOf(
                    ItemRecipeReferenceWithRecipe(
                        reference = ShoppingListItemRecipeReferenceEntity(
                            shoppingListItemId = "1",
                            recipeId = "1",
                            quantity = 1.0,
                        ),
                        recipe = teaWithMilkRecipe,
                    ),
                ),
            ),
            ShoppingListItemWithRecipes(
                item = ShoppingListItemEntity(
                    remoteId = "2",
                    shoppingListId = "1",
                    checked = true,
                    position = 1,
                    isFood = true,
                    note = "Cold",
                    quantity = 500.0,
                    unit = "ml",
                    food = "Milk",
                ),
                recipes = listOf(
                    ItemRecipeReferenceWithRecipe(
                        reference = ShoppingListItemRecipeReferenceEntity(
                            shoppingListItemId = "2",
                            recipeId = "1",
                            quantity = 500.0,
                        ),
                        recipe = teaWithMilkRecipe,
                    ),
                ),
            )
        ),
    )
    AppTheme {
        ShoppingListItems(shoppingList = shoppingList)
    }
}
