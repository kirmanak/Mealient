package gq.kirmanak.mealient.data.recipes.impl

interface RecipeImageUrlProvider {

    suspend fun generateImageUrl(imageId: String?): String?
}