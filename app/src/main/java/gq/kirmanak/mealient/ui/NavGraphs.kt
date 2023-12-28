package gq.kirmanak.mealient.ui

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListsScreenDestination
import gq.kirmanak.mealient.ui.destinations.AddRecipeScreenDestination
import gq.kirmanak.mealient.ui.destinations.AuthenticationScreenDestination
import gq.kirmanak.mealient.ui.destinations.BaseURLScreenDestination
import gq.kirmanak.mealient.ui.destinations.DisclaimerScreenDestination
import gq.kirmanak.mealient.ui.destinations.RecipeScreenDestination
import gq.kirmanak.mealient.ui.destinations.RecipesListDestination

internal object NavGraphs {

    val recipes: NavGraphSpec = NavGraphImpl(
        route = "recipes",
        startRoute = RecipesListDestination,
        destinations = listOf(
            RecipesListDestination,
            RecipeScreenDestination,
        ),
    )

    val shoppingLists: NavGraphSpec = NavGraphImpl(
        route = "shopping_lists",
        startRoute = ShoppingListsScreenDestination,
        destinations = listOf(
            ShoppingListsScreenDestination,
            ShoppingListScreenDestination,
        ),
    )

    val root: NavGraphSpec = NavGraphImpl(
        route = "root",
        startRoute = recipes,
        destinations = listOf(
            AddRecipeScreenDestination,
            DisclaimerScreenDestination,
            BaseURLScreenDestination,
            AuthenticationScreenDestination,
        ),
        nestedNavGraphs = listOf(
            recipes,
            shoppingLists,
        ),
    )

}

private data class NavGraphImpl(
    override val route: String,
    override val startRoute: Route,
    val destinations: List<DestinationSpec<*>>,
    override val nestedNavGraphs: List<NavGraphSpec> = emptyList()
) : NavGraphSpec {

    override val destinationsByRoute: Map<String, DestinationSpec<*>> =
        destinations.associateBy { it.route }
}