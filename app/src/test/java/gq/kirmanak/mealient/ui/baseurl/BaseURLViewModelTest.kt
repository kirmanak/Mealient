package gq.kirmanak.mealient.ui.baseurl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.impl.BaseUrlLogRedactor
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.TrustedCertificatesStore
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.ui.OperationUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import javax.net.ssl.SSLHandshakeException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseURLViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var recipeRepo: RecipeRepo

    @MockK(relaxUnitFun = true)
    lateinit var trustedCertificatesStore: TrustedCertificatesStore

    @RelaxedMockK
    lateinit var baseUrlLogRedactor: BaseUrlLogRedactor

    lateinit var subject: BaseURLViewModel

    @Before
    override fun setUp() {
        super.setUp()
        subject = BaseURLViewModel(
            serverInfoRepo = serverInfoRepo,
            authRepo = authRepo,
            recipeRepo = recipeRepo,
            logger = logger,
            trustedCertificatesStore = trustedCertificatesStore,
            baseUrlLogRedactor = baseUrlLogRedactor,
        )
    }

    @Test
    fun `when saveBaseURL expect no version checks given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { serverInfoRepo.tryBaseURL(any()) }
    }

    @Test
    fun `when saveBaseURL expect URL isn't saved given that current URL matches new`() = runTest {
        setupSaveBaseUrlWithOldUrl()
        coVerify(inverse = true) { serverInfoRepo.tryBaseURL(any()) }
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
        coEvery { serverInfoRepo.tryBaseURL(any()) } returns Result.success(Unit)
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
    }

    @Test
    fun `when saveBaseUrl expect URL is saved given that new URL doesn't match old`() = runTest {
        setupSaveBaseUrlWithNewUrl()
        coVerify { serverInfoRepo.tryBaseURL(eq(TEST_BASE_URL)) }
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
        coEvery { serverInfoRepo.tryBaseURL(any()) } returns Result.success(Unit)
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
    }

    @Test
    fun `when saveBaseURL expect error given that version can't be fetched`() = runTest {
        coEvery { serverInfoRepo.getUrl() } returns null
        coEvery { serverInfoRepo.tryBaseURL(any()) } returns Result.failure(IOException())
        subject.saveBaseUrl(TEST_BASE_URL)
        advanceUntilIdle()
        assertThat(subject.uiState.value).isInstanceOf(OperationUiState.Failure::class.java)
    }

    @Test
    fun `when saving base url with no prefix and https throws expect http attempt`() = runTest {
        coEvery { serverInfoRepo.getUrl() } returns null
        val err = NetworkError.MalformedUrl(SSLHandshakeException("test"))
        coEvery { serverInfoRepo.tryBaseURL("https://test") } returns Result.failure(err)
        coEvery { serverInfoRepo.tryBaseURL("http://test") } returns Result.success(Unit)
        subject.saveBaseUrl("test")
        coVerifyOrder {
            serverInfoRepo.tryBaseURL("https://test")
            serverInfoRepo.tryBaseURL("http://test")
        }
    }

    @Test
    fun `when saving base url with no prefix and https throws non ssl expect no http`() = runTest {
        coEvery { serverInfoRepo.getUrl() } returns null
        val err = NetworkError.NotMealie(IOException())
        coEvery { serverInfoRepo.tryBaseURL("https://test") } returns Result.failure(err)
        subject.saveBaseUrl("test")
        coVerify(inverse = true) { serverInfoRepo.tryBaseURL("http://test") }
    }

    @Test
    fun `when saving base url with https prefix and https throws expect no http call`() = runTest {
        coEvery { serverInfoRepo.getUrl() } returns null
        val err = NetworkError.MalformedUrl(SSLHandshakeException("test"))
        coEvery { serverInfoRepo.tryBaseURL("https://test") } returns Result.failure(err)
        subject.saveBaseUrl("https://test")
        coVerify(inverse = true) { serverInfoRepo.tryBaseURL("http://test") }
    }
}