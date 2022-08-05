package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeDataSourceImpl @Inject constructor(
    private val recipeServiceFactory: ServiceFactory<RecipeService>,
    private val logger: Logger,
) : RecipeDataSource {

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse> {
        logger.v { "requestRecipes() called with: start = $start, limit = $limit" }
        val recipeSummary = getRecipeService().getRecipeSummary(start, limit)
        logger.v { "requestRecipes() returned: $recipeSummary" }
        return recipeSummary
    }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse {
        logger.v { "requestRecipeInfo() called with: slug = $slug" }
        val recipeInfo = getRecipeService().getRecipe(slug)
        logger.v { "requestRecipeInfo() returned: $recipeInfo" }
        return recipeInfo
    }

    private suspend fun getRecipeService(): RecipeService {
        logger.v { "getRecipeService() called" }
        return recipeServiceFactory.provideService()
    }
}
