package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieDataSourceWrapper: MealieDataSourceWrapper,
) : RecipeDataSource {

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse> {
        logger.v { "requestRecipes() called with: start = $start, limit = $limit" }
        val recipeSummary = mealieDataSourceWrapper.requestRecipes(start, limit)
        logger.v { "requestRecipes() returned: $recipeSummary" }
        return recipeSummary
    }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse {
        logger.v { "requestRecipeInfo() called with: slug = $slug" }
        val recipeInfo = mealieDataSourceWrapper.requestRecipeInfo(slug)
        logger.v { "requestRecipeInfo() returned: $recipeInfo" }
        return recipeInfo
    }

}
