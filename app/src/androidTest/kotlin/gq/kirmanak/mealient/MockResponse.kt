package gq.kirmanak.mealient

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

val versionV1Response = MockResponse()
    .setResponseCode(200)
    .setHeader("Content-Type", "application/json")
    .setBody("""{"production":true,"version":"v1.0.0beta-5","demoStatus":false,"allowSignup":true}""")

val notFoundResponse = MockResponse()
    .setResponseCode(404)
    .setHeader("Content-Type", "application/json")
    .setBody("""{"detail":"Not found"}"""")

val expectedLoginRequest = """
    username=test%40test.test&password=password
""".trimIndent()

val loginTokenResponse = MockResponse()
    .setResponseCode(200)
    .setHeader("Content-Type", "application/json")
    .setBody("""{"access_token":"login-token"}""")

val apiTokenResponse = MockResponse()
    .setResponseCode(200)
    .setHeader("Content-Type", "application/json")
    .setBody("""{"token":"api-token"}""")

val recipeSummariesResponse = MockResponse()
    .setResponseCode(200)
    .setHeader("Content-Type", "application/json")
    .setBody("""{"items":[]}""")

val expectedApiTokenAuthorizationHeader = "Bearer login-token"

val expectedAuthorizationHeader = "Bearer api-token"

data class RequestToDispatch(
    val path: String,
    val body: String,
    val authorization: String?,
) {
    constructor(recordedRequest: RecordedRequest) : this(
        path = recordedRequest.path.orEmpty(),
        body = recordedRequest.body.readUtf8(),
        authorization = recordedRequest.getHeader("Authorization")
    )
}

fun MockWebServer.dispatch(block: (RequestToDispatch) -> MockResponse) {
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val requestToDispatch = RequestToDispatch(request)
            Log.d("MockWebServer", "request = $requestToDispatch")
            return block(requestToDispatch)
        }
    }
}