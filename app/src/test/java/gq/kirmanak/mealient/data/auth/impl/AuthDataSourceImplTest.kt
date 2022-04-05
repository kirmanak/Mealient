package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.network.NetworkError.*
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.di.NetworkModule
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.test.toJsonResponseBody
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class AuthDataSourceImplTest {
    @MockK
    lateinit var authService: AuthService

    @MockK
    lateinit var authServiceFactory: ServiceFactory<AuthService>

    lateinit var subject: AuthDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AuthDataSourceImpl(authServiceFactory, NetworkModule.createJson())
        coEvery { authServiceFactory.provideService(any(), eq(false)) } returns authService
    }

    @Test
    fun `when authentication is successful then token is correct`() = runTest {
        val token = authenticate(Response.success(GetTokenResponse(TEST_TOKEN)))
        assertThat(token).isEqualTo(TEST_TOKEN)
    }

    @Test(expected = Unauthorized::class)
    fun `when authenticate receives 401 and Unauthorized then throws Unauthorized`() = runTest {
        val body = "{\"detail\":\"Unauthorized\"}".toJsonResponseBody()
        authenticate(Response.error(401, body))
    }

    @Test(expected = NotMealie::class)
    fun `when authenticate receives 401 but not Unauthorized then throws NotMealie`() = runTest {
        val body = "{\"detail\":\"Something\"}".toJsonResponseBody()
        authenticate(Response.error(401, body))
    }

    @Test(expected = NotMealie::class)
    fun `when authenticate receives 404 and empty body then throws NotMealie`() = runTest {
        authenticate(Response.error(401, "".toJsonResponseBody()))
    }

    @Test(expected = NotMealie::class)
    fun `when authenticate receives 200 and null then throws NotMealie`() = runTest {
        authenticate(Response.success<GetTokenResponse>(200, null))
    }

    @Test(expected = NoServerConnection::class)
    fun `when authenticate and getToken throws then throws NoServerConnection`() = runTest {
        coEvery { authService.getToken(any(), any()) } throws IOException("Server not found")
        callAuthenticate()
    }

    @Test(expected = MalformedUrl::class)
    fun `when authenticate and provideService throws then MalformedUrl`() = runTest {
        coEvery {
            authServiceFactory.provideService(any(), eq(false))
        } throws MalformedUrl(RuntimeException())
        callAuthenticate()
    }

    private suspend fun authenticate(response: Response<GetTokenResponse>): String {
        coEvery { authService.getToken(eq(TEST_USERNAME), eq(TEST_PASSWORD)) } returns response
        return callAuthenticate()
    }

    private suspend fun callAuthenticate() = subject.authenticate(TEST_USERNAME, TEST_PASSWORD)

}