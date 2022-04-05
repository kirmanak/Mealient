package gq.kirmanak.mealient.data.network

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import io.mockk.*
import io.mockk.impl.annotations.MockK
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class AuthenticationInterceptorTest {
    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK
    lateinit var chain: Interceptor.Chain

    lateinit var subject: AuthenticationInterceptor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AuthenticationInterceptor(authRepo)
    }

    @Test
    fun `when intercept without header then response without header`() {
        val request = createRequest()
        val response = createResponse(request)
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response
        coEvery { authRepo.getAuthHeader() } returns null
        assertThat(subject.intercept(chain)).isEqualTo(response)
    }

    @Test
    fun `when intercept with header then chain called with header`() {
        val request = createRequest()
        val response = createResponse(request)
        val requestSlot = slot<Request>()

        every { chain.request() } returns request
        every { chain.proceed(capture(requestSlot)) } returns response
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        subject.intercept(chain)

        assertThat(requestSlot.captured.header("Authorization")).isEqualTo(TEST_AUTH_HEADER)
    }

    @Test
    fun `when intercept with stale header then calls invalidate`() {
        val request = createRequest()
        val response = createResponse(request, code = 403)

        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        subject.intercept(chain)

        coVerifySequence {
            authRepo.getAuthHeader()
            authRepo.invalidateAuthHeader(TEST_AUTH_HEADER)
            authRepo.getAuthHeader()
        }
    }

    @Test
    fun `when intercept with proper header then requests auth header once`() {
        val request = createRequest()
        val response = createResponse(request)

        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        subject.intercept(chain)

        coVerifySequence { authRepo.getAuthHeader() }
    }


    @Test
    fun `when intercept with stale header then updates header`() {
        val request = createRequest()
        val response = createResponse(request, code = 403)
        val requests = mutableListOf<Request>()

        every { chain.request() } returns request
        every { chain.proceed(capture(requests)) } returns response
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER andThen "Bearer NEW TOKEN"

        subject.intercept(chain)

        assertThat(requests.size).isEqualTo(2)
        assertThat(requests[0].header("Authorization")).isEqualTo(TEST_AUTH_HEADER)
        assertThat(requests[1].header("Authorization")).isEqualTo("Bearer NEW TOKEN")
    }

    private fun createRequest(
        url: String = TEST_BASE_URL,
    ): Request = Request.Builder()
        .url(url)
        .build()

    private fun createResponse(
        request: Request,
        code: Int = 200,
    ): Response = Response.Builder()
        .protocol(Protocol.HTTP_2)
        .code(code)
        .request(request)
        .message("Doesn't matter")
        .build()
}