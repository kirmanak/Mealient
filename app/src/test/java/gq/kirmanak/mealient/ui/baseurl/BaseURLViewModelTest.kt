package gq.kirmanak.mealient.ui.baseurl

import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
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
    lateinit var baseURLStorage: BaseURLStorage

    @MockK
    lateinit var versionDataSource: VersionDataSource

    lateinit var subject: BaseURLViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = BaseURLViewModel(baseURLStorage, versionDataSource)
    }

    @Test
    fun `when saveBaseUrl and getVersionInfo returns result then saves to storage`() = runTest {
        coEvery {
            versionDataSource.getVersionInfo(eq(TEST_BASE_URL))
        } returns VersionInfo(true, "0.5.6", true)
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
        coVerify { baseURLStorage.storeBaseURL(eq(TEST_BASE_URL)) }
    }
}