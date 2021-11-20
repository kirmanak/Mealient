package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.impl.RetrofitBuilder
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalSerializationApi
class RecipeDataSourceImpl @Inject constructor(
    private val authRepo: AuthRepo,
    private val retrofitBuilder: RetrofitBuilder
) : RecipeDataSource {
    private var _recipeService: RecipeService? = null

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse> {
        Timber.v("requestRecipes() called with: start = $start, limit = $limit")
        val service: RecipeService = getRecipeService()
        val recipeSummary = service.getRecipeSummary(start, limit)
        Timber.v("requestRecipes() returned: $recipeSummary")
        return recipeSummary
    }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse {
        Timber.v("requestRecipeInfo() called with: slug = $slug")
        val service: RecipeService = getRecipeService()
        val recipeInfo = service.getRecipe(slug)
        Timber.v("requestRecipeInfo() returned: $recipeInfo")
        return recipeInfo
    }

    private suspend fun getRecipeService(): RecipeService {
        Timber.v("getRecipeService() called")
        val cachedService: RecipeService? = _recipeService
        val service: RecipeService = if (cachedService == null) {
            val baseUrl = checkNotNull(authRepo.getBaseUrl()) { "Base url is null" }
            val token = checkNotNull(authRepo.getToken()) { "Token is null" }
            Timber.d("requestRecipes: baseUrl = $baseUrl, token = $token")
            val retrofit = retrofitBuilder.buildRetrofit(baseUrl)
            val createdService = retrofit.create(RecipeService::class.java)
            _recipeService = createdService
            createdService
        } else {
            cachedService
        }
        return service
    }
}