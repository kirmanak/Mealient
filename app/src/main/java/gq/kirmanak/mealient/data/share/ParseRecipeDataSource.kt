package gq.kirmanak.mealient.data.share

import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo

interface ParseRecipeDataSource {

    suspend fun parseRecipeFromURL(parseRecipeURLInfo: ParseRecipeURLInfo): String
}