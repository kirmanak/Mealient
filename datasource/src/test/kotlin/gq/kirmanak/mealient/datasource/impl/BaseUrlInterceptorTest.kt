package gq.kirmanak.mealient.datasource.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test

class BaseUrlInterceptorTest : BaseUnitTest() {

    private lateinit var subject: Interceptor

    @MockK
    lateinit var serverUrlProvider: ServerUrlProvider

    @MockK(relaxUnitFun = true)
    lateinit var chain: Interceptor.Chain

    @Before
    override fun setUp() {
        super.setUp()
        subject = BaseUrlInterceptor(logger) { serverUrlProvider }
    }

    @Test
    fun `when intercept is called expect it changes the url`() {
        val requestSlot = slot<Request>()
        every { chain.proceed(capture(requestSlot)) } returns mockk()
        every { chain.request() } returns buildRequest()
        coEvery { serverUrlProvider.getUrl() } returns "https://mealie:3241/"
        subject.intercept(chain)
        assertThat(requestSlot.captured.url.toString()).isEqualTo("https://mealie:3241/")
    }

    private fun buildRequest(
        url: String = "http://localhost",
    ) = Request.Builder().apply {
        url(url)
    }.build()
}