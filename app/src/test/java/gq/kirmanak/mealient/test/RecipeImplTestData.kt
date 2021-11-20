package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.data.recipes.db.entity.RecipeEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.data.recipes.impl.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeIngredientResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeInstructionResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
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

    val CAKE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = 1,
        name = "Cake",
        slug = "cake",
        image = "86",
        description = "A tasty cake",
        rating = 4,
        dateAdded = LocalDate.parse("2021-11-13"),
        dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13")
    )

    val PORRIDGE_RECIPE_SUMMARY_ENTITY = RecipeSummaryEntity(
        remoteId = 2,
        name = "Porridge",
        slug = "porridge",
        image = "89",
        description = "A tasty porridge",
        rating = 5,
        dateAdded = LocalDate.parse("2021-11-12"),
        dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
    )

    val TEST_RECIPE_ENTITIES = listOf(CAKE_RECIPE_SUMMARY_ENTITY, PORRIDGE_RECIPE_SUMMARY_ENTITY)

    fun MockWebServer.enqueueSuccessfulRecipeSummaryResponse() {
        val response = MockResponse()
            .setBody(RECIPE_SUMMARY_SUCCESSFUL)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(200)
        enqueue(response)
    }

    fun MockWebServer.enqueueUnsuccessfulRecipeResponse() {
        val response = MockResponse()
            .setBody(RECIPE_SUMMARY_UNSUCCESSFUL)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(401)
        enqueue(response)
    }

    val SUGAR_INGREDIENT = GetRecipeIngredientResponse(
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1
    )

    val BREAD_INGREDIENT = GetRecipeIngredientResponse(
        title = "Bread",
        note = "2 oz of white bread",
        unit = "",
        food = "",
        disableAmount = false,
        quantity = 2
    )

    val MILK_INGREDIENT = GetRecipeIngredientResponse(
        title = "Milk",
        note = "2 oz of white milk",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 3
    )

    val MIX_INSTRUCTION = GetRecipeInstructionResponse(
        title = "Mix",
        text = "Mix the ingredients"
    )

    val BAKE_INSTRUCTION = GetRecipeInstructionResponse(
        title = "Bake",
        text = "Bake the ingredients"
    )

    val BOIL_INSTRUCTION = GetRecipeInstructionResponse(
        title = "Boil",
        text = "Boil the ingredients"
    )

    val GET_CAKE_RESPONSE = GetRecipeResponse(
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
        recipeYield = "4 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, BREAD_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BAKE_INSTRUCTION)
    )

    val GET_CAKE_RESPONSE_BODY = """
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
                "dateUpdated": "2021-11-13T15:30:13",
            "recipeYield": "4 servings",
            "recipeIngredient": [
                {
                    "title": "Sugar",
        "note": "2 oz of white sugar",
        "unit": null,
        "food": null,
        "disableAmount": true,
        "quantity": 1
                },
                {
                    "title": "Bread",
        "note": "2 oz of white bread",
        "unit": null,
        "food": null,
        "disableAmount": false,
        "quantity": 2
                }
            ],
            "recipeInstructions": [
                {
                    "title": "Mix",
                    "text": "Mix the ingredients"
                },
                {
                    "title": "Bake",
                    "text": "Bake the ingredients"
                }
            ],
            "nutrition": {
                "calories": "100",
                "fatContent": "20",
                "proteinContent": "30",
                "carbohydrateContent": "40",
                "fiberContent": "50",
                "sodiumContent": "23",
                "sugarContent": "53"
            },
            "tools": [],
            "totalTime": "12 hours",
            "prepTime": "1 hour",
            "performTime": "4 hours",
            "settings": {
                "public": true,
                "showNutrition": true,
                "showAssets": true,
                "landscapeView": true,
                "disableComments": false,
                "disableAmount": false
            },
            "assets": [],
            "notes": [
                {
                    "title": "Note title",
                    "text": "Note text"
                },
                {
                    "title": "Second note",
                    "text": "Second note text"
                }
            ],
            "orgURL": null,
            "extras": {},
            "comments": [
                {
                    "text": "A new comment",
                    "id": 1,
                    "uuid": "476ebc15-f794-4eda-8380-d77bba47f839",
                    "recipeSlug": "test-recipe",
                    "dateAdded": "2021-11-19T22:13:23.862459",
                    "user": {
                        "id": 1,
                        "username": "kirmanak",
                        "admin": true
                    }
                },
                {
                    "text": "A second comment",
                    "id": 2,
                    "uuid": "20498eba-9639-4acd-ba0a-4829ee06915a",
                    "recipeSlug": "test-recipe",
                    "dateAdded": "2021-11-19T22:13:29.912314",
                    "user": {
                        "id": 1,
                        "username": "kirmanak",
                        "admin": true
                    }
                }
            ]
        }
    """.trimIndent()

    val GET_PORRIDGE_RESPONSE = GetRecipeResponse(
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
        recipeYield = "3 servings",
        recipeIngredients = listOf(SUGAR_INGREDIENT, MILK_INGREDIENT),
        recipeInstructions = listOf(MIX_INSTRUCTION, BOIL_INSTRUCTION)
    )

    val MIX_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 1,
        recipeId = 1,
        title = "Mix",
        text = "Mix the ingredients",
    )

    val BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 2,
        recipeId = 1,
        title = "Bake",
        text = "Bake the ingredients",
    )

    val CAKE_RECIPE_ENTITY = RecipeEntity(
        remoteId = 1,
        recipeYield = "4 servings"
    )

    val CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 1,
        recipeId = 1,
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1
    )

    val CAKE_BREAD_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 2,
        recipeId = 1,
        title = "Bread",
        note = "2 oz of white bread",
        unit = "",
        food = "",
        disableAmount = false,
        quantity = 2
    )

    val FULL_CAKE_INFO_ENTITY = FullRecipeInfo(
        recipeEntity = CAKE_RECIPE_ENTITY,
        recipeSummaryEntity = CAKE_RECIPE_SUMMARY_ENTITY,
        recipeIngredients = listOf(
            CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY,
            CAKE_BREAD_RECIPE_INGREDIENT_ENTITY,
        ),
        recipeInstructions = listOf(
            MIX_CAKE_RECIPE_INSTRUCTION_ENTITY,
            BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY,
        ),
    )

    val PORRIDGE_RECIPE_ENTITY_FULL = RecipeEntity(
        remoteId = 2,
        recipeYield = "3 servings"
    )

    val PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 4,
        recipeId = 2,
        title = "Milk",
        note = "2 oz of white milk",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 3
    )

    val PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY = RecipeIngredientEntity(
        localId = 3,
        recipeId = 2,
        title = "Sugar",
        note = "2 oz of white sugar",
        unit = "",
        food = "",
        disableAmount = true,
        quantity = 1
    )

    val PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 3,
        recipeId = 2,
        title = "Mix",
        text = "Mix the ingredients"
    )

    val PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY = RecipeInstructionEntity(
        localId = 4,
        recipeId = 2,
        title = "Boil",
        text = "Boil the ingredients"
    )

    val FULL_PORRIDGE_INFO_ENTITY = FullRecipeInfo(
        recipeEntity = PORRIDGE_RECIPE_ENTITY_FULL,
        recipeSummaryEntity = PORRIDGE_RECIPE_SUMMARY_ENTITY,
        recipeIngredients = listOf(
            PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY,
            PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY,
        ),
        recipeInstructions = listOf(
            PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY,
            PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY,
        )
    )

    fun MockWebServer.enqueueSuccessfulGetRecipe() {
        val response = MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody(GET_CAKE_RESPONSE_BODY)
        enqueue(response)
    }
}