package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.datasource.models.GetUserInfoResponse

object AuthImplTestData {
    const val TEST_USERNAME = "TEST_USERNAME"
    const val TEST_PASSWORD = "TEST_PASSWORD"
    const val TEST_BASE_URL = "https://example.com"
    const val TEST_TOKEN = "TEST_TOKEN"
    const val TEST_API_TOKEN = "TEST_API_TOKEN"

    val FAVORITE_RECIPES_LIST = listOf("cake", "porridge")
    val USER_INFO = GetUserInfoResponse("userId", FAVORITE_RECIPES_LIST)
}