package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.extensions.logAndMapErrors
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecipeDataSourceImpl @Inject constructor(
    private val addRecipeServiceFactory: ServiceFactory<AddRecipeService>,
) : AddRecipeDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String {
        Timber.v("addRecipe() called with: recipe = $recipe")
        val service = addRecipeServiceFactory.provideService()
        val response = logAndMapErrors(
            block = { service.addRecipe(recipe) }, logProvider = { "addRecipe: can't add recipe" }
        )
        Timber.v("addRecipe() response = $response")
        return response
    }
}