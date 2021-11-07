package gq.kirmanak.mealie.data.recipes.network

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int = 0, end: Int = 9999): List<GetRecipeSummaryResponse>
}