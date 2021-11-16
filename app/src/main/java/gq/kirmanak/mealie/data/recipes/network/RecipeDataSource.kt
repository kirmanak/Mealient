package gq.kirmanak.mealie.data.recipes.network

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int = 0, limit: Int = 9999): List<GetRecipeSummaryResponse>
}