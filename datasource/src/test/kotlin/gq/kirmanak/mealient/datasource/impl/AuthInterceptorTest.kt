package gq.kirmanak.mealient.datasource.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.datasource.impl.AuthInterceptor.Companion.HEADER_NAME
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest : BaseUnitTest() {

    private lateinit var subject: Interceptor

    @MockK(relaxed = true)
    lateinit var authenticationProvider: AuthenticationProvider

    @MockK(relaxed = true)
    lateinit var chain: Interceptor.Chain

    @Before
    override fun setUp() {
        super.setUp()
        subject = AuthInterceptor(logger) { authenticationProvider }
    }

    @Test
    fun `when intercept is called expect header to be retrieved`() {
        subject.intercept(chain)
        coVerify { authenticationProvider.getAuthHeader() }
    }

    @Test
    fun `when intercept is called and no header expect no header`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        coEvery { chain.request() } returns buildRequest()
        val requestSlot = slot<Request>()
        coEvery { chain.proceed(capture(requestSlot)) } returns buildResponse()
        subject.intercept(chain)
        assertThat(requestSlot.captured.header(HEADER_NAME)).isNull()
    }

    @Test
    fun `when intercept is called and no header expect no logout`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        coEvery { chain.request() } returns buildRequest()
        coEvery { chain.proceed(any()) } returns buildResponse(code = 200)
        subject.intercept(chain)
        coVerify(inverse = true) { authenticationProvider.logout() }
    }

    @Test
    fun `when intercept is called with no header and auth fails expect no logout`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        coEvery { chain.request() } returns buildRequest()
        coEvery { chain.proceed(any()) } returns buildResponse(code = 401)
        subject.intercept(chain)
        coVerify(inverse = true) { authenticationProvider.logout() }
    }

    @Test
    fun `when intercept is called and there is a header expect a header`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "header"
        coEvery { chain.request() } returns buildRequest()
        val requestSlot = slot<Request>()
        coEvery { chain.proceed(capture(requestSlot)) } returns buildResponse()
        subject.intercept(chain)
        assertThat(requestSlot.captured.header(HEADER_NAME)).isEqualTo("header")
    }

    @Test
    fun `when intercept is called and there is a header that authenticates expect no logout`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "header"
        coEvery { chain.request() } returns buildRequest()
        coEvery { chain.proceed(any()) } returns buildResponse(code = 200)
        subject.intercept(chain)
        coVerify(inverse = true) { authenticationProvider.logout() }
    }

    @Test
    fun `when intercept is called and there was a header but still 401 expect logout`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "header"
        coEvery { chain.request() } returns buildRequest()
        coEvery { chain.proceed(any()) } returns buildResponse(code = 401)
        subject.intercept(chain)
        coVerify { authenticationProvider.logout() }
    }

    private fun buildResponse(
        url: String = "http://localhost",
        code: Int = 200,
        message: String = if (code == 200) "OK" else "Unauthorized",
        protocol: Protocol = Protocol.HTTP_2,
    ) = Response.Builder().apply {
        request(buildRequest(url))
        code(code)
        message(message)
        protocol(protocol)
    }.build()

    private fun buildRequest(
        url: String = "http://localhost",
    ) = Request.Builder().url(url).build()
}