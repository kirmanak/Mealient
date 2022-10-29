package gq.kirmanak.mealient.data.recipes.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class RecipeSummaryInfo(
    val remoteId: String,
    val name: String,
    val slug: String,
    val image: String?,
    val description: String = "",
    val recipeCategories: List<String>,
    val tags: List<String>,
    val rating: Int?,
    val dateAdded: LocalDate,
    val dateUpdated: LocalDateTime,
    val imageId: String,
)
