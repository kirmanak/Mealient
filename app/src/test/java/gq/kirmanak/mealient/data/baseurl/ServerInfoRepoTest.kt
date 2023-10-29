package gq.kirmanak.mealient.data.baseurl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

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
        coEvery { storage.getBaseURL() } returns null
        subject.tryBaseURL(TEST_BASE_URL)
        coVerify {
            storage.storeBaseURL(eq(TEST_BASE_URL))
        }
    }
}