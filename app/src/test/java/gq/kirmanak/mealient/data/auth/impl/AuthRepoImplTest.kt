package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.MalformedUrl
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.Unauthorized
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.test.AuthImplTestData.enqueueSuccessfulAuthResponse
import gq.kirmanak.mealient.test.AuthImplTestData.enqueueUnsuccessfulAuthResponse
import gq.kirmanak.mealient.test.MockServerTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthRepoImplTest : MockServerTest() {
    @Inject
    lateinit var subject: AuthRepoImpl

    @Test
    fun `when not authenticated then first auth status is false`() = runBlocking {
        assertThat(subject.authenticationStatuses().first()).isFalse()
    }

    @Test
    fun `when authenticated then first auth status is true`() = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        assertThat(subject.authenticationStatuses().first()).isTrue()
    }

    @Test(expected = Unauthorized::class)
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

    @Test(expected = MalformedUrl::class)
    fun `when baseUrl has ftp scheme then throws`() {
        subject.parseBaseUrl("ftp://test")
    }

    @Test
    fun `when baseUrl scheme has one slash then corrects`() {
        assertThat(subject.parseBaseUrl("https:/test")).isEqualTo("https://test/")
    }

    @Test
    fun `when baseUrl is single word then appends scheme and slash`() {
        assertThat(subject.parseBaseUrl("test")).isEqualTo("https://test/")
    }

    @Test
    fun `when baseUrl is host appends scheme and slash`() {
        assertThat(subject.parseBaseUrl("google.com")).isEqualTo("https://google.com/")
    }

    @Test
    fun `when baseUrl is correct then doesn't change`() {
        assertThat(subject.parseBaseUrl("https://google.com/")).isEqualTo("https://google.com/")
    }
}
