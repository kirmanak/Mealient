package gq.kirmanak.mealient.data.baseurl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_VERSION
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.test.RecipeImplTestData.VERSION_INFO_V0
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ServerInfoRepoTest : BaseUnitTest() {

    private lateinit var subject: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var storage: ServerInfoStorage

    @MockK(relaxUnitFun = true)
    lateinit var dataSource: VersionDataSource

    @Before
    override fun setUp() {
        super.setUp()
        subject = ServerInfoRepoImpl(storage, dataSource, logger)
    }

    @Test
    fun `when storage returns null url expect getUrl return null`() = runTest {
        coEvery { storage.getBaseURL() } returns null
        assertThat(subject.getUrl()).isNull()
    }

    @Test
    fun `when storage returns url value expect getUrl return value`() = runTest {
        val expected = TEST_BASE_URL
        coEvery { storage.getBaseURL() } returns expected
        assertThat(subject.getUrl()).isEqualTo(expected)
    }

    @Test
    fun `when getUrl expect storage is accessed`() = runTest {
        coEvery { storage.getBaseURL() } returns null
        subject.getUrl()
        coVerify { storage.getBaseURL() }
    }

    @Test
    fun `when tryBaseURL succeeds expect call to storage`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns null
        coEvery { dataSource.getVersionInfo() } returns VersionInfo(TEST_VERSION)
        subject.tryBaseURL(TEST_BASE_URL)
        coVerify {
            storage.storeBaseURL(eq(TEST_BASE_URL))
            dataSource.getVersionInfo()
            storage.storeServerVersion(TEST_VERSION)
        }
    }

    @Test
    fun `when tryBaseURL fails expect call to storage`() = runTest {
        coEvery { storage.getServerVersion() } returns "serverVersion"
        coEvery { storage.getBaseURL() } returns "baseUrl"
        coEvery { dataSource.getVersionInfo() } throws IOException()
        subject.tryBaseURL(TEST_BASE_URL)
        coVerify {
            storage.storeBaseURL(eq(TEST_BASE_URL))
            dataSource.getVersionInfo()
            storage.storeBaseURL(eq("baseUrl"), eq("serverVersion"))
        }
    }

    @Test
    fun `when storage is empty expect getVersion to call data source`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VERSION_INFO_V0
        subject.getVersion()
        coVerify { dataSource.getVersionInfo() }
    }

    @Test
    fun `when storage is empty and data source has value expect getVersion to save it`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VersionInfo(TEST_VERSION)
        subject.getVersion()
        coVerify { storage.storeServerVersion(TEST_VERSION) }
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when data source has invalid value expect getVersion to throw`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VersionInfo("v2.0.0")
        subject.getVersion()
    }

    @Test
    fun `when data source has invalid value expect getVersion not to save`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VersionInfo("v2.0.0")
        subject.runCatching { getVersion() }
        coVerify(inverse = true) { storage.storeServerVersion(any()) }
    }

    @Test
    fun `when storage has value expect getVersion to not get URL`() = runTest {
        coEvery { storage.getServerVersion() } returns TEST_VERSION
        subject.getVersion()
        coVerify(inverse = true) { storage.getBaseURL() }
    }

    @Test
    fun `when storage has value expect getVersion to not call data source`() = runTest {
        coEvery { storage.getServerVersion() } returns TEST_VERSION
        subject.getVersion()
        coVerify(inverse = true) { dataSource.getVersionInfo() }
    }

    @Test
    fun `when storage has v0 value expect getVersion to return parsed`() = runTest {
        coEvery { storage.getServerVersion() } returns "v0.5.6"
        assertThat(subject.getVersion()).isEqualTo(ServerVersion.V0)
    }

    @Test
    fun `when storage has v1 value expect getVersion to return parsed`() = runTest {
        coEvery { storage.getServerVersion() } returns "v1.0.0-beta05"
        assertThat(subject.getVersion()).isEqualTo(ServerVersion.V1)
    }

    @Test
    fun `when data source has valid v0 value expect getVersion to return it`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VersionInfo("v0.5.6")
        assertThat(subject.getVersion()).isEqualTo(ServerVersion.V0)
    }

    @Test
    fun `when data source has valid v1 value expect getVersion to return it`() = runTest {
        coEvery { storage.getServerVersion() } returns null
        coEvery { storage.getBaseURL() } returns TEST_BASE_URL
        coEvery { dataSource.getVersionInfo() } returns VersionInfo("v1.0.0-beta05")
        assertThat(subject.getVersion()).isEqualTo(ServerVersion.V1)
    }
}