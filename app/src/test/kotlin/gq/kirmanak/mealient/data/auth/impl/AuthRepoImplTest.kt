package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_USERNAME
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepoImplTest {

    @MockK
    lateinit var dataSource: AuthDataSource

    @MockK(relaxUnitFun = true)
    lateinit var storage: AuthStorage

    lateinit var subject: AuthRepo

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AuthRepoImpl(storage, dataSource)
    }

    @Test
    fun `when isAuthorizedFlow then reads from storage`() = runTest {
        every { storage.authHeaderFlow } returns flowOf("", null, "header")
        assertThat(subject.isAuthorizedFlow.toList()).isEqualTo(listOf(true, false, true))
    }

    @Test
    fun `when authenticate successfully then saves to storage`() = runTest {
        coEvery { dataSource.authenticate(eq(TEST_USERNAME), eq(TEST_PASSWORD)) } returns TEST_TOKEN
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD)
        coVerifyAll {
            storage.setAuthHeader(TEST_AUTH_HEADER)
            storage.setEmail(TEST_USERNAME)
            storage.setPassword(TEST_PASSWORD)
        }
        confirmVerified(storage)
    }

    @Test
    fun `when authenticate fails then does not change storage`() = runTest {
        coEvery { dataSource.authenticate(any(), any()) } throws RuntimeException()
        runCatching { subject.authenticate("invalid", "") }
        confirmVerified(storage)
    }

    @Test
    fun `when logout then removes email, password and header`() = runTest {
        subject.logout()
        coVerifyAll {
            storage.setEmail(null)
            storage.setPassword(null)
            storage.setAuthHeader(null)
        }
        confirmVerified(storage)
    }

    @Test
    fun `when invalidate then does not authenticate without email`() = runTest {
        coEvery { storage.getEmail() } returns null
        coEvery { storage.getPassword() } returns TEST_PASSWORD
        subject.invalidateAuthHeader()
        confirmVerified(dataSource)
    }

    @Test
    fun `when invalidate then does not authenticate without password`() = runTest {
        coEvery { storage.getEmail() } returns TEST_USERNAME
        coEvery { storage.getPassword() } returns null
        subject.invalidateAuthHeader()
        confirmVerified(dataSource)
    }

    @Test
    fun `when invalidate with credentials then calls authenticate`() = runTest {
        coEvery { storage.getEmail() } returns TEST_USERNAME
        coEvery { storage.getPassword() } returns TEST_PASSWORD
        coEvery { dataSource.authenticate(eq(TEST_USERNAME), eq(TEST_PASSWORD)) } returns TEST_TOKEN
        subject.invalidateAuthHeader()
        coVerifyAll {
            dataSource.authenticate(eq(TEST_USERNAME), eq(TEST_PASSWORD))
        }
    }

    @Test
    fun `when invalidate with credentials and auth fails then clears email`() = runTest {
        coEvery { storage.getEmail() } returns "invalid"
        coEvery { storage.getPassword() } returns ""
        coEvery { dataSource.authenticate(any(), any()) } throws RuntimeException()
        subject.invalidateAuthHeader()
        coVerify { storage.setEmail(null) }
    }
}