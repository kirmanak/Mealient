package gq.kirmanak.mealie.data.auth.impl

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.HttpException
import java.nio.charset.Charset
import javax.inject.Inject

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class AuthDataSourceImplTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var subject: AuthDataSourceImpl
    private lateinit var mockServer: MockWebServer
    private lateinit var serverUrl: String

    @Before
    fun inject() {
        hiltRule.inject()
    }

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

    @Test
    fun `when authentication is successful then token is correct`() = runBlocking {
        enqueueSuccessfulResponse()
        val token = subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(token).isEqualTo(TEST_TOKEN)
    }

    @Test(expected = HttpException::class)
    fun `when authentication isn't successful then throws`(): Unit = runBlocking {
        enqueueUnsuccessfulResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
    }

    @Test
    fun `when authentication is requested then body is correct`() = runBlocking {
        enqueueSuccessfulResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        val body = mockServer.takeRequest().body()
        assertThat(body).isEqualTo("username=$TEST_USERNAME&password=$TEST_PASSWORD")
    }

    @Test
    fun `when authentication is requested then path is correct`() = runBlocking {
        enqueueSuccessfulResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        val path = mockServer.takeRequest().path
        assertThat(path).isEqualTo("/api/auth/token")
    }

    private fun RecordedRequest.body() = body.readString(Charset.defaultCharset())

    private fun enqueueUnsuccessfulResponse() {
        val response = MockResponse()
            .setBody(UNSUCCESSFUL_AUTH_RESPONSE)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(401)
        mockServer.enqueue(response)
    }

    private fun enqueueSuccessfulResponse() {
        val response = MockResponse()
            .setBody(SUCCESSFUL_AUTH_RESPONSE)
            .setHeader("Content-Type", "application/json")
            .setResponseCode(200)
        mockServer.enqueue(response)
    }

    companion object {
        private const val TEST_USERNAME = "TEST_USERNAME"
        private const val TEST_PASSWORD = "TEST_PASSWORD"
        private const val TEST_TOKEN = "TEST_TOKEN"
        private const val SUCCESSFUL_AUTH_RESPONSE =
            "{\"access_token\":\"$TEST_TOKEN\",\"token_type\":\"TEST_TOKEN_TYPE\"}"
        private const val UNSUCCESSFUL_AUTH_RESPONSE =
            "{\"detail\":\"Unauthorized\"}"
    }
}