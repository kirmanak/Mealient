package gq.kirmanak.mealient.data.baseurl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.baseurl.impl.VersionDataSourceImpl
import gq.kirmanak.mealient.data.baseurl.impl.VersionResponse
import gq.kirmanak.mealient.data.baseurl.impl.VersionService
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.toJsonResponseBody
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class VersionDataSourceImplTest {
    @MockK
    lateinit var versionService: VersionService

    @MockK
    lateinit var versionServiceFactory: ServiceFactory<VersionService>

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    lateinit var subject: VersionDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = VersionDataSourceImpl(versionServiceFactory, logger)
        coEvery { versionServiceFactory.provideService(eq(TEST_BASE_URL)) } returns versionService
    }

    @Test(expected = NetworkError.MalformedUrl::class)
    fun `when getVersionInfo and provideService throws then MalformedUrl`() = runTest {
        coEvery {
            versionServiceFactory.provideService(eq(TEST_BASE_URL))
        } throws NetworkError.MalformedUrl(RuntimeException())
        subject.getVersionInfo(TEST_BASE_URL)
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when getVersionInfo and getVersion throws HttpException then NotMealie`() = runTest {
        val error = HttpException(Response.error<VersionResponse>(404, "".toJsonResponseBody()))
        coEvery { versionService.getVersion() } throws error
        subject.getVersionInfo(TEST_BASE_URL)
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when getVersionInfo and getVersion throws SerializationException then NotMealie`() =
        runTest {
            coEvery { versionService.getVersion() } throws SerializationException()
            subject.getVersionInfo(TEST_BASE_URL)
        }

    @Test(expected = NetworkError.NoServerConnection::class)
    fun `when getVersionInfo and getVersion throws IOException then NoServerConnection`() =
        runTest {
            coEvery { versionService.getVersion() } throws IOException()
            subject.getVersionInfo(TEST_BASE_URL)
        }

    @Test
    fun `when getVersionInfo and getVersion returns result then result`() = runTest {
        coEvery { versionService.getVersion() } returns VersionResponse(true, "v0.5.6", true)
        assertThat(subject.getVersionInfo(TEST_BASE_URL)).isEqualTo(
            VersionInfo(true, "v0.5.6", true)
        )
    }
}