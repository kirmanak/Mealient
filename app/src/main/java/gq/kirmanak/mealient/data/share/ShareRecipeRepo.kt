package gq.kirmanak.mealient.data.share

interface ShareRecipeRepo {

    suspend fun saveRecipeByURL(url: CharSequence): String
}