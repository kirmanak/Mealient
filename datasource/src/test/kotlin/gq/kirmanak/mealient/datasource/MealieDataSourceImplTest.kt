package gq.kirmanak.mealient.datasource

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.models.GetTokenResponse
import gq.kirmanak.mealient.datasource.models.NetworkError
import gq.kirmanak.mealient.datasource.models.VersionResponse
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.toJsonResponseBody
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException

@OptIn(ExperimentalCoroutinesApi::class)
class MealieDataSourceImplTest {

    @MockK
    lateinit var service: MealieService

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    lateinit var subject: MealieDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MealieDataSourceImpl(logger, service, Json.Default)
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when getVersionInfo and getVersion throws HttpException then NotMealie`() = runTest {
        val error = HttpException(Response.error<VersionResponse>(404, "".toJsonResponseBody()))
        coEvery { service.getVersion(any()) } throws error
        subject.getVersionInfo(TEST_BASE_URL)
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when getVersionInfo and getVersion throws SerializationException then NotMealie`() =
        runTest {
            coEvery { service.getVersion(any()) } throws SerializationException()
            subject.getVersionInfo(TEST_BASE_URL)
        }

    @Test(expected = NetworkError.NoServerConnection::class)
    fun `when getVersionInfo and getVersion throws IOException then NoServerConnection`() =
        runTest {
            coEvery { service.getVersion(any()) } throws ConnectException()
            subject.getVersionInfo(TEST_BASE_URL)
        }

    @Test
    fun `when getVersionInfo and getVersion returns result then result`() = runTest {
        val versionResponse = VersionResponse(true, "v0.5.6", true)
        coEvery { service.getVersion(any()) } returns versionResponse
        assertThat(subject.getVersionInfo(TEST_BASE_URL)).isSameInstanceAs(versionResponse)
    }

    @Test
    fun `when authentication is successful then token is correct`() = runTest {
        coEvery { service.getToken(any(), any(), any()) } returns GetTokenResponse(TEST_TOKEN)
        assertThat(callAuthenticate()).isEqualTo(TEST_TOKEN)
    }

    @Test(expected = NetworkError.Unauthorized::class)
    fun `when authenticate receives 401 and Unauthorized then throws Unauthorized`() = runTest {
        val body = "{\"detail\":\"Unauthorized\"}".toJsonResponseBody()
        coEvery {
            service.getToken(any(), any(), any())
        } throws HttpException(Response.error<GetTokenResponse>(401, body))
        callAuthenticate()
    }

    @Test(expected = HttpException::class)
    fun `when authenticate receives 401 but not Unauthorized then throws NotMealie`() = runTest {
        val body = "{\"detail\":\"Something\"}".toJsonResponseBody()
        coEvery {
            service.getToken(any(), any(), any())
        } throws HttpException(Response.error<GetTokenResponse>(401, body))
        callAuthenticate()
    }

    @Test(expected = SerializationException::class)
    fun `when authenticate receives 404 and empty body then throws NotMealie`() = runTest {
        val body = "".toJsonResponseBody()
        coEvery {
            service.getToken(any(), any(), any())
        } throws HttpException(Response.error<GetTokenResponse>(401, body))
        callAuthenticate()
    }

    @Test(expected = IOException::class)
    fun `when authenticate and getToken throws then throws NoServerConnection`() = runTest {
        coEvery { service.getToken(any(), any(), any()) } throws IOException("Server not found")
        callAuthenticate()
    }

    private suspend fun callAuthenticate(): String =
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, TEST_BASE_URL)

    companion object {
        const val TEST_USERNAME = "TEST_USERNAME"
        const val TEST_PASSWORD = "TEST_PASSWORD"
        const val TEST_BASE_URL = "https://example.com/"
        const val TEST_TOKEN = "TEST_TOKEN"
        const val TEST_AUTH_HEADER = "Bearer TEST_TOKEN"
    }
}