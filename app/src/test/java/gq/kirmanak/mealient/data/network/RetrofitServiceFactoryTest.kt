package gq.kirmanak.mealient.data.network

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionService
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
class RetrofitServiceFactoryTest {

    @MockK
    lateinit var retrofitBuilder: RetrofitBuilder

    @MockK
    lateinit var baseURLStorage: BaseURLStorage

    @MockK
    lateinit var retrofit: Retrofit

    @MockK
    lateinit var versionService: VersionService

    lateinit var subject: ServiceFactory<VersionService>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = retrofitBuilder.createServiceFactory(baseURLStorage)
        coEvery { retrofitBuilder.buildRetrofit(any(), eq(true)) } returns retrofit
        every { retrofit.create(eq(VersionService::class.java)) } returns versionService
        coEvery { baseURLStorage.requireBaseURL() } returns TEST_BASE_URL
    }

    @Test
    fun `when provideService and url is null then url storage requested`() = runTest {
        subject.provideService()
        coVerify { baseURLStorage.requireBaseURL() }
    }

    @Test
    fun `when provideService and url is null then service still provided`() = runTest {
        assertThat(subject.provideService()).isEqualTo(versionService)
    }

    @Test
    fun `when provideService called twice then builder called once`() = runTest {
        subject.provideService()
        subject.provideService()
        coVerifyAll { retrofitBuilder.buildRetrofit(eq(TEST_BASE_URL), eq(true)) }
    }

    @Test
    fun `when provideService called secondly with new url then builder called twice`() = runTest {
        subject.provideService()
        subject.provideService("new url")
        coVerifyAll {
            retrofitBuilder.buildRetrofit(eq(TEST_BASE_URL), eq(true))
            retrofitBuilder.buildRetrofit(eq("new url"), eq(true))
        }
    }
}