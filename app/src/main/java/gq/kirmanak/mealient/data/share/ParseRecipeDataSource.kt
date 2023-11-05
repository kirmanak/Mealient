package gq.kirmanak.mealient.data.share

import gq.kirmanak.mealient.datasource.models.ParseRecipeURLRequest

interface ParseRecipeDataSource {

    suspend fun parseRecipeFromURL(parseRecipeURLInfo: ParseRecipeURLRequest): String
}