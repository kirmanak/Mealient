package gq.kirmanak.mealie.data.recipes

import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import gq.kirmanak.mealie.data.recipes.network.GetRecipeSummaryResponse
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

object RecipeImplTestData {
    val RECIPE_SUMMARY_CAKE = GetRecipeSummaryResponse(
        remoteId = 1,
        name = "Cake",
        slug = "cake",
        image = "86",
        description = "A tasty cake",
        recipeCategories = listOf("dessert", "tasty"),
        tags = listOf("gluten", "allergic"),
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
    )

    val RECIPE_SUMMARY_PORRIDGE = GetRecipeSummaryResponse(
        remoteId = 2,
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        recipeCategories = listOf("porridge", "tasty"),
        tags = listOf("gluten", "milk"),
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    )

    val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE)

    const val RECIPE_SUMMARY_SUCCESSFUL = """[
            {
                "id": 1,
                "name": "Cake",
                "slug": "cake",
                "image": "86",
                "description": "A tasty cake",
                "recipeCategory": ["dessert", "tasty"],
                "tags": ["gluten", "allergic"],
                "rating": 4,
                "dateAdded": "2021-11-13",
                "dateUpdated": "2021-11-13T15:30:13"
            },
            {
                "id": 2,
                "name": "Porridge",
                "slug": "porridge",
                "image": "89",
                "description": "A tasty porridge",
                "recipeCategory": ["porridge", "tasty"],
                "tags": ["gluten", "milk"],
                "rating": 5,
                "dateAdded": "2021-11-12",
                "dateUpdated": "2021-10-13T17:35:23"
            }
        ]"""

    const val RECIPE_SUMMARY_UNSUCCESSFUL = """
        {"detail":"Unauthorized"}
    """

    val CAKE_RECIPE_ENTITY = RecipeEntity(
        localId = 1,
        remoteId = 1,
        name = "Cake",
        slug = "cake",
        image = "86",
        description = "A tasty cake",
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13")
    )

    val PORRIDGE_RECIPE_ENTITY = RecipeEntity(
        localId = 2,
        remoteId = 2,
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    )

    val TEST_RECIPE_ENTITIES = listOf(CAKE_RECIPE_ENTITY, PORRIDGE_RECIPE_ENTITY)

    fun MockWebServer.enqueueSuccessfulRecipeSummaryResponse() {
        val response = MockResponse()
            .setBody(RECIPE_SUMMARY_SUCCESSFUL)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(200)
        enqueue(response)
    }

    fun MockWebServer.enqueueUnsuccessfulRecipeSummaryResponse() {
        val response = MockResponse()
            .setBody(RECIPE_SUMMARY_UNSUCCESSFUL)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(401)
        enqueue(response)
    }
}