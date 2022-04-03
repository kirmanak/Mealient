package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeDataSourceImpl @Inject constructor(
    private val authRepo: AuthRepo,
    private val recipeServiceFactory: ServiceFactory<RecipeService>,
) : RecipeDataSource {

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse> {
        Timber.v("requestRecipes() called with: start = $start, limit = $limit")
        val recipeSummary = getRecipeService().getRecipeSummary(start, limit, getToken())
        Timber.v("requestRecipes() returned: $recipeSummary")
        return recipeSummary
    }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse {
        Timber.v("requestRecipeInfo() called with: slug = $slug")
        val recipeInfo = getRecipeService().getRecipe(slug, getToken())
        Timber.v("requestRecipeInfo() returned: $recipeInfo")
        return recipeInfo
    }

    private suspend fun getRecipeService(): RecipeService {
        Timber.v("getRecipeService() called")
        return recipeServiceFactory.provideService()
    }

    private suspend fun getToken(): String? = authRepo.getAuthHeader()
}
