package gq.kirmanak.mealie.data.auth.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.enqueueSuccessfulAuthResponse
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.enqueueUnsuccessfulAuthResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException
import javax.inject.Inject

@HiltAndroidTest
class AuthRepoImplTest : MockServerTest() {
    @Inject
    lateinit var subject: AuthRepoImpl

    @Test
    fun `when not authenticated then isAuthenticated false`() = runBlocking {
        assertThat(subject.isAuthenticated()).isFalse()
    }

    @Test
    fun `when authenticated then isAuthenticated true`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(subject.isAuthenticated()).isTrue()
    }

    @Test(expected = HttpException::class)
    fun `when authentication fails then authenticate throws`() = runBlocking {
        mockServer.enqueueUnsuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
    }

    @Test
    fun `when authenticated then getToken returns token`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(subject.getToken()).isEqualTo(TEST_TOKEN)
    }

    @Test
    fun `when authenticated then getBaseUrl returns url`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(subject.getBaseUrl()).isEqualTo(serverUrl)
    }
}
