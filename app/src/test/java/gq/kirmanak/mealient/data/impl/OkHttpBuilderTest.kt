package gq.kirmanak.mealient.data.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AUTHORIZATION_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_URL
import gq.kirmanak.mealient.test.MockServerTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class OkHttpBuilderTest : MockServerTest() {

    @Inject
    lateinit var subject: OkHttpBuilder

    @Inject
    lateinit var authStorage: AuthStorage

    @Test
    fun `when token null then no auth header`() {
        val client = subject.buildOkHttp()
        val header = sendRequestAndExtractAuthHeader(client)
        assertThat(header).isNull()
    }

    @Test
    fun `when token isn't null then auth header contains token`() {
        authStorage.storeAuthData(TEST_TOKEN, TEST_URL)
        val client = subject.buildOkHttp()
        val header = sendRequestAndExtractAuthHeader(client)
        assertThat(header).isEqualTo("Bearer $TEST_TOKEN")
    }

    private fun sendRequestAndExtractAuthHeader(client: OkHttpClient): String? {
        mockServer.enqueue(MockResponse())
        val request = Request.Builder().url(serverUrl).get().build()
        client.newCall(request).execute()
        return mockServer.takeRequest().getHeader(AUTHORIZATION_HEADER)
    }
}