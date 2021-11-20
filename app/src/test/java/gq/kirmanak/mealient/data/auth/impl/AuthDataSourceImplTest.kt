package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.data.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.data.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.data.test.AuthImplTestData.body
import gq.kirmanak.mealient.data.test.AuthImplTestData.enqueueSuccessfulAuthResponse
import gq.kirmanak.mealient.data.test.AuthImplTestData.enqueueUnsuccessfulAuthResponse
import gq.kirmanak.mealient.data.test.MockServerTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import retrofit2.HttpException
import javax.inject.Inject

@ExperimentalSerializationApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
class AuthDataSourceImplTest : MockServerTest() {
    @Inject
    lateinit var subject: AuthDataSourceImpl

    @Test
    fun `when authentication is successful then token is correct`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        val token = subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(token).isEqualTo(TEST_TOKEN)
    }

    @Test(expected = HttpException::class)
    fun `when authentication isn't successful then throws`(): Unit = runBlocking {
        mockServer.enqueueUnsuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
    }

    @Test
    fun `when authentication is requested then body is correct`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        val body = mockServer.takeRequest().body()
        assertThat(body).isEqualTo("username=$TEST_USERNAME&password=$TEST_PASSWORD")
    }

    @Test
    fun `when authentication is requested then path is correct`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        val path = mockServer.takeRequest().path
        assertThat(path).isEqualTo("/api/auth/token")
    }

}