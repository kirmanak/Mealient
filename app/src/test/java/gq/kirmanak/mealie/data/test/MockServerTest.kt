package gq.kirmanak.mealie.data.test

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

abstract class MockServerTest : HiltRobolectricTest() {
    lateinit var mockServer: MockWebServer
    lateinit var serverUrl: String

    @Before
    fun startMockServer() {
        mockServer = MockWebServer().apply {
            start()
        }
        serverUrl = mockServer.url("/").toString()
    }

    @After
    fun stopMockServer() {
        mockServer.shutdown()
    }
}