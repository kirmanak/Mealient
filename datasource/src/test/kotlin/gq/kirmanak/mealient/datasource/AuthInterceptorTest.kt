package gq.kirmanak.mealient.datasource

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.impl.AuthInterceptor
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse

class AuthInterceptorTest : BaseUnitTest() {

    private lateinit var subject: AuthInterceptor

    @MockK(relaxUnitFun = true)
    lateinit var authenticationProvider: AuthenticationProvider

    @MockK(relaxUnitFun = true)
    lateinit var chain: Interceptor.Chain

    @Before
    override fun setUp() {
        super.setUp()
        subject = AuthInterceptor { authenticationProvider }
        every { chain.request() } returns Request.Builder().url("http://localhost").build()
    }

    @Test
    fun `when no header then still proceeds`() {
        mockProceedCallAndCaptureRequest()
        coEvery { authenticationProvider.getAuthHeader() } returns null
        subject.intercept(chain)
        verify { chain.proceed(any()) }
    }

    @Test
    fun `when has header then adds header`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token"
        val requestSlot = mockProceedCallAndCaptureRequest()
        subject.intercept(chain)
        assertThat(requestSlot.captured.header(AuthInterceptor.HEADER_NAME)).isEqualTo("token")
    }

    @Test(expected = HttpException::class)
    fun `when unauthorized but didn't have token then throws`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        mockUnauthorized()
        subject.intercept(chain)
    }

    @Test(expected = HttpException::class)
    fun `when unauthorized and had token then invalidates it`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token"
        mockUnauthorized()
        subject.intercept(chain)
        coVerify { authenticationProvider.invalidateAuthHeader() }
    }

    @Test(expected = HttpException::class)
    fun `when not found and had token then throws exception`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token"
        val requestSlot = slot<Request>()
        every {
            chain.proceed(capture(requestSlot))
        } answers {
            throw HttpException(RetrofitResponse.error<String>(404, "".toResponseBody()))
        }
        subject.intercept(chain)
    }

    @Test
    fun `when not found and had token then does not invalidate it`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token"
        val requestSlot = slot<Request>()
        every {
            chain.proceed(capture(requestSlot))
        } answers {
            throw HttpException(RetrofitResponse.error<String>(404, "".toResponseBody()))
        }
        try {
            subject.intercept(chain)
        } catch (e: HttpException) {
            coVerify(inverse = true) { authenticationProvider.invalidateAuthHeader() }
        }
    }

    @Test
    fun `when unauthorized and had token then calls again with new token`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token" andThen "newToken"
        val requests = mutableListOf<Request>()
        every {
            chain.proceed(capture(requests))
        } answers {
            throw HttpException(RetrofitResponse.error<String>(401, "".toResponseBody()))
        } andThenAnswer {
            buildResponse(requests[1])
        }
        subject.intercept(chain)
        coVerifySequence {
            authenticationProvider.getAuthHeader()
            authenticationProvider.invalidateAuthHeader()
            authenticationProvider.getAuthHeader()
        }
        assertThat(requests[0].header(AuthInterceptor.HEADER_NAME)).isEqualTo("token")
        assertThat(requests[1].header(AuthInterceptor.HEADER_NAME)).isEqualTo("newToken")
    }

    @Test
    fun `when had token but now does not then removes it`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        val mockRequest = Request.Builder()
            .url("http://localhost")
            .header(AuthInterceptor.HEADER_NAME, "token")
            .build()
        every { chain.request() } returns mockRequest
        val requestSlot = mockProceedCallAndCaptureRequest()
        subject.intercept(chain)
        assertThat(requestSlot.captured.header(AuthInterceptor.HEADER_NAME)).isNull()
    }

    private fun mockUnauthorized() {
        val requestSlot = slot<Request>()
        every {
            chain.proceed(capture(requestSlot))
        } answers {
            throw HttpException(RetrofitResponse.error<String>(401, "".toResponseBody()))
        }
    }

    private fun mockProceedCallAndCaptureRequest(): CapturingSlot<Request> {
        val requestSlot = slot<Request>()
        every {
            chain.proceed(capture(requestSlot))
        } answers {
            buildResponse(requestSlot.captured)
        }
        return requestSlot
    }

    private fun buildResponse(
        request: Request,
        code: Int = 200,
        protocol: Protocol = Protocol.HTTP_2,
        message: String = if (code == 200) "OK" else "NOT OK",
    ) = Response.Builder()
        .code(code)
        .request(request)
        .protocol(protocol)
        .message(message)
        .build()
}