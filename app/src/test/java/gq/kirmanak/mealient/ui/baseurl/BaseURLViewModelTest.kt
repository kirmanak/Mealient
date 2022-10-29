package gq.kirmanak.mealient.ui.baseurl

import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_VERSION
import gq.kirmanak.mealient.test.RobolectricTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseURLViewModelTest : RobolectricTest() {

    @MockK(relaxUnitFun = true)
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK
    lateinit var versionDataSource: VersionDataSource

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    lateinit var subject: BaseURLViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = BaseURLViewModel(serverInfoRepo, versionDataSource, logger)
    }

    @Test
    fun `when saveBaseUrl and getVersionInfo returns result then saves to storage`() = runTest {
        coEvery {
            versionDataSource.getVersionInfo(eq(TEST_BASE_URL))
        } returns VersionInfo(TEST_VERSION)
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
        coVerify { serverInfoRepo.storeBaseURL(eq(TEST_BASE_URL), eq(TEST_VERSION)) }
    }
}