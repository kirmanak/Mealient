package gq.kirmanak.mealient.datasource

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.datasource.ktor.AuthKtorConfiguration
import gq.kirmanak.mealient.test.BaseUnitTest
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private const val AUTH_TOKEN = "token"

internal class AuthKtorConfigurationTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var authenticationProvider: AuthenticationProvider

    private lateinit var subject: AuthKtorConfiguration

    @Before
    override fun setUp() {
        super.setUp()
        coEvery { authenticationProvider.getAuthToken() } returns AUTH_TOKEN
        subject = AuthKtorConfiguration(FakeProvider(authenticationProvider), logger)
    }

    @Test
    fun `getTokens returns BearerTokens with auth token`() = runTest {
        val bearerTokens = subject.getTokens()
        assertThat(bearerTokens?.accessToken).isEqualTo(AUTH_TOKEN)
    }

    @Test
    fun `getTokens returns BearerTokens without refresh token`() = runTest {
        val bearerTokens = subject.getTokens()
        assertThat(bearerTokens?.refreshToken).isEmpty()
    }

    @Test
    fun `refreshTokens returns new auth token if it doesn't match old`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.Unauthorized, "old token")
        val actual = with(subject) { refreshTokensParams.refreshTokens() }
        assertThat(actual?.accessToken).isEqualTo(AUTH_TOKEN)
    }

    @Test
    fun `refreshTokens returns empty refresh token if auth token doesn't match old`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.Unauthorized, "old token")
        val actual = with(subject) { refreshTokensParams.refreshTokens() }
        assertThat(actual?.refreshToken).isEmpty()
    }

    @Test
    fun `refreshTokens returns null if auth token matches old`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.Unauthorized, AUTH_TOKEN)
        val actual = with(subject) { refreshTokensParams.refreshTokens() }
        assertThat(actual).isNull()
    }

    @Test
    fun `refreshTokens calls logout if auth token matches old`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.Unauthorized, AUTH_TOKEN)
        with(subject) { refreshTokensParams.refreshTokens() }
        coVerify { authenticationProvider.logout() }
    }

    @Test
    fun `refreshTokens does not logout if status code is not found`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.NotFound, AUTH_TOKEN)
        with(subject) { refreshTokensParams.refreshTokens() }
        coVerify(inverse = true) { authenticationProvider.logout() }
    }

    @Test
    fun `refreshTokens returns same access token if status code is not found`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.NotFound, AUTH_TOKEN)
        val actual = with(subject) { refreshTokensParams.refreshTokens() }
        assertThat(actual?.accessToken).isEqualTo(AUTH_TOKEN)
    }

    @Test
    fun `refreshTokens returns empty refresh token if status code is not found`() = runTest {
        val refreshTokensParams = mockRefreshTokenParams(HttpStatusCode.NotFound, AUTH_TOKEN)
        val actual = with(subject) { refreshTokensParams.refreshTokens() }
        assertThat(actual?.refreshToken).isEmpty()
    }

    private fun mockRefreshTokenParams(
        responseStatusCode: HttpStatusCode,
        oldAccessToken: String,
    ): RefreshTokensParams {
        val notFoundResponse = mockk<HttpResponse> {
            every { status } returns responseStatusCode
        }
        val refreshTokensParams = mockk<RefreshTokensParams> {
            every { response } returns notFoundResponse
            every { oldTokens } returns BearerTokens(oldAccessToken, "")
        }
        return refreshTokensParams
    }
}