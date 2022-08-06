package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.extensions.logAndMapErrors
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieDataSourceWrapper: MealieDataSourceWrapper,
) : AddRecipeDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String {
        logger.v { "addRecipe() called with: recipe = $recipe" }
        val response = logger.logAndMapErrors(
            block = { mealieDataSourceWrapper.addRecipe(recipe) },
            logProvider = { "addRecipe: can't add recipe" }
        )
        logger.v { "addRecipe() response = $response" }
        return response
    }
}