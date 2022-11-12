package gq.kirmanak.mealient.ui.baseurl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_VERSION
import gq.kirmanak.mealient.test.FakeLogger
import gq.kirmanak.mealient.ui.OperationUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseURLViewModelTest {

    @MockK(relaxUnitFun = true)
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var recipeRepo: RecipeRepo

    @MockK
    lateinit var versionDataSource: VersionDataSource

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val logger: Logger = FakeLogger()

    lateinit var subject: BaseURLViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        subject = BaseURLViewModel(
            serverInfoRepo = serverInfoRepo,
            authRepo = authRepo,
            recipeRepo = recipeRepo,
            versionDataSource = versionDataSource,
            logger = logger,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when saveBaseURL expect no version checks given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { versionDataSource.getVersionInfo(any()) }
    }

    @Test
    fun `when saveBaseURL expect URL isn't saved given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { serverInfoRepo.storeBaseURL(any(), any()) }
    }

    @Test
    fun `when saveBaseURL expect no logout given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { authRepo.logout() }
    }

    @Test
    fun `when saveBaseURL expect data intact given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { recipeRepo.clearLocalData() }
    }

    private fun TestScope.setupSaveBaseUrlWithOldUrl() {
        coEvery { serverInfoRepo.getUrl() } returns TEST_BASE_URL
        versionDataSourceReturnsSuccess()
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
    }

    @Test
    fun `when saveBaseUrl expect URL is saved given that new URL doesn't match old`() = runTest {
        setupSaveBaseUrlWithNewUrl()
        coVerify { serverInfoRepo.storeBaseURL(eq(TEST_BASE_URL), eq(TEST_VERSION)) }
    }

    @Test
    fun `when saveBaseURL expect logout given that new URL doesn't match old`() = runTest {
        setupSaveBaseUrlWithNewUrl()
        coVerify { authRepo.logout() }
    }

    @Test
    fun `when saveBaseURL expect recipes removed given that new URL doesn't match old`() = runTest {
        setupSaveBaseUrlWithNewUrl()
        coVerify { recipeRepo.clearLocalData() }
    }

    private fun TestScope.setupSaveBaseUrlWithNewUrl() {
        coEvery { serverInfoRepo.getUrl() } returns null
        versionDataSourceReturnsSuccess()
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
    }

    private fun versionDataSourceReturnsSuccess() {
        coEvery {
            versionDataSource.getVersionInfo(eq(TEST_BASE_URL))
        } returns VersionInfo(TEST_VERSION)
    }

    @Test
    fun `when saveBaseURL expect error given that version can't be fetched`() = runTest {
        coEvery { serverInfoRepo.getUrl() } returns null
        coEvery { versionDataSource.getVersionInfo(eq(TEST_BASE_URL)) } throws IOException()
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
        assertThat(subject.uiState.value).isInstanceOf(OperationUiState.Failure::class.java)
    }
}