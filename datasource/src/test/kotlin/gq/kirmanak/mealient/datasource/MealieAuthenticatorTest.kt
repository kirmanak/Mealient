package gq.kirmanak.mealient.datasource

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.impl.MealieAuthenticator
import gq.kirmanak.mealient.datasource.impl.MealieAuthenticator.Companion.HEADER_NAME
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class MealieAuthenticatorTest : BaseUnitTest() {

    private lateinit var subject: MealieAuthenticator

    @MockK(relaxUnitFun = true)
    lateinit var authenticationProvider: AuthenticationProvider

    @Before
    override fun setUp() {
        super.setUp()
        subject = MealieAuthenticator { authenticationProvider }
    }

    @Test
    fun `when bearer is not supported expect authenticate to return null`() {
        val response = buildResponse(challenges = null)
        assertThat(subject.authenticate(null, response)).isNull()
    }

    @Test
    fun `when no auth header exists expect authenticate to return null`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        val response = buildResponse()
        assertThat(subject.authenticate(null, response)).isNull()
    }

    @Test
    fun `when no auth header exists expect authenticate to call provider`() {
        coEvery { authenticationProvider.getAuthHeader() } returns null
        val response = buildResponse()
        subject.authenticate(null, response)
        coVerify { authenticationProvider.getAuthHeader() }
    }

    @Test
    fun `when an auth header was set expect authenticate to return null`() {
        val response = buildResponse(authHeader = "token")
        assertThat(subject.authenticate(null, response)).isNull()
    }

    @Test
    fun `when an auth header was set expect authenticate to logout`() {
        val response = buildResponse(authHeader = "token")
        subject.authenticate(null, response)
        coVerify { authenticationProvider.logout() }
    }

    @Test
    fun `when auth header exists expect authenticate to return request`() {
        coEvery { authenticationProvider.getAuthHeader() } returns "token"
        val response = buildResponse()
        val actualHeader = subject.authenticate(null, response)?.header(HEADER_NAME)
        assertThat(actualHeader).isEqualTo("token")
    }

    private fun buildResponse(
        url: String = "http://localhost",
        code: Int = 401,
        message: String = "Unauthorized",
        protocol: Protocol = Protocol.HTTP_2,
        challenges: String? = "Bearer",
        authHeader: String? = null,
    ): Response {
        val request = buildRequest(authHeader, url)
        return Response.Builder().apply {
            request(request)
            code(code)
            message(message)
            protocol(protocol)
            if (challenges != null) header("WWW-Authenticate", challenges)
        }.build()
    }

    private fun buildRequest(
        authHeader: String? = null,
        url: String = "http://localhost",
    ): Request = Request.Builder().apply {
        url(url)
        if (authHeader != null) header(HEADER_NAME, authHeader)
    }.build()

}