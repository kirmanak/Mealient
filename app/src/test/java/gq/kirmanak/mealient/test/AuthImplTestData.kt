package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.data.baseurl.ServerVersion

object AuthImplTestData {
    const val TEST_USERNAME = "TEST_USERNAME"
    const val TEST_PASSWORD = "TEST_PASSWORD"
    const val TEST_BASE_URL = "https://example.com/"
    const val TEST_TOKEN = "TEST_TOKEN"
    const val TEST_AUTH_HEADER = "Bearer TEST_TOKEN"
    const val TEST_API_TOKEN = "TEST_API_TOKEN"
    const val TEST_API_AUTH_HEADER = "Bearer TEST_API_TOKEN"
    const val TEST_VERSION = "v0.5.6"
    val TEST_SERVER_VERSION_V0 = ServerVersion.V0
    val TEST_SERVER_VERSION_V1 = ServerVersion.V1
}