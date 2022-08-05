package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.extensions.logAndMapErrors
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeDataSourceImpl @Inject constructor(
    private val addRecipeServiceFactory: ServiceFactory<AddRecipeService>,
    private val logger: Logger,
) : AddRecipeDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String {
        logger.v { "addRecipe() called with: recipe = $recipe" }
        val service = addRecipeServiceFactory.provideService()
        val response = logger.logAndMapErrors(
            block = { service.addRecipe(recipe) },
            logProvider = { "addRecipe: can't add recipe" }
        )
        logger.v { "addRecipe() response = $response" }
        return response
    }
}