package gq.kirmanak.mealient.data.share

interface ParseRecipeDataSource {

    suspend fun parseRecipeFromURL(parseRecipeURLInfo: ParseRecipeURLInfo): String
}