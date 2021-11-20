package gq.kirmanak.mealient.data.test

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.nio.charset.Charset

object AuthImplTestData {
    const val TEST_USERNAME = "TEST_USERNAME"
    const val TEST_PASSWORD = "TEST_PASSWORD"
    const val TEST_TOKEN = "TEST_TOKEN"
    const val SUCCESSFUL_AUTH_RESPONSE =
        "{\"access_token\":\"$TEST_TOKEN\",\"token_type\":\"TEST_TOKEN_TYPE\"}"
    const val UNSUCCESSFUL_AUTH_RESPONSE =
        "{\"detail\":\"Unauthorized\"}"
    const val TEST_URL = "TEST_URL"

    fun RecordedRequest.body() = body.readString(Charset.defaultCharset())

    fun MockWebServer.enqueueUnsuccessfulAuthResponse() {
        val response = MockResponse()
            .setBody(UNSUCCESSFUL_AUTH_RESPONSE)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(401)
        enqueue(response)
    }

    fun MockWebServer.enqueueSuccessfulAuthResponse() {
        val response = MockResponse()
            .setBody(SUCCESSFUL_AUTH_RESPONSE)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(200)
        enqueue(response)
    }
}