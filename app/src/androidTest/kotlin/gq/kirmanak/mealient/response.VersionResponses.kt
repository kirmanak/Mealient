package gq.kirmanak.mealient

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

val versionV1Response = MockResponse().setResponseCode(200).setBody(
    """{"production":true,"version":"v1.0.0beta-5","demoStatus":false,"allowSignup":true}"""
)

val notFoundResponse = MockResponse().setResponseCode(404).setBody("""{"detail":"Not found"}"""")

fun MockWebServer.dispatch(block: (String, RecordedRequest) -> MockResponse) {
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return block(request.path.orEmpty(), request)
        }
    }
}