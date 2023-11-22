package gq.kirmanak.mealient.ui.recipes.list

internal sealed interface RecipeListSnackbar {

    data class FavoriteAdded(val name: String) : RecipeListSnackbar

    data class FavoriteRemoved(val name: String) : RecipeListSnackbar

    data object FavoriteUpdateFailed : RecipeListSnackbar

    data object DeleteFailed : RecipeListSnackbar
}