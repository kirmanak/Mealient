package gq.kirmanak.mealient.datasource

import com.google.common.truth.Truth.assertThat
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

    companion object {
        private const val TEST_BASE_URL = ""
    }
}