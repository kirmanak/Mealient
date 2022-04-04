package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.network.NetworkError.Unauthorized
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.test.RobolectricTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepoImplTest : RobolectricTest() {

    @MockK
    lateinit var dataSource: AuthDataSource

    @MockK(relaxUnitFun = true)
    lateinit var storage: AuthStorage

    lateinit var subject: AuthRepoImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AuthRepoImpl(dataSource, storage)
    }

    @Test
    fun `when not authenticated then first auth status is false`() = runTest {
        coEvery { storage.authHeaderFlow } returns flowOf(null)
        assertThat(subject.isAuthorizedFlow.first()).isFalse()
    }

    @Test
    fun `when authenticated then first auth status is true`() = runTest {
        coEvery { storage.authHeaderFlow } returns flowOf(TEST_AUTH_HEADER)
        assertThat(subject.isAuthorizedFlow.first()).isTrue()
    }

    @Test(expected = Unauthorized::class)
    fun `when authentication fails then authenticate throws`() = runTest {
        coEvery {
            dataSource.authenticate(eq(TEST_USERNAME), eq(TEST_PASSWORD))
        } throws Unauthorized(RuntimeException())
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD)
    }

    @Test
    fun `when authenticated then getToken returns token`() = runTest {
        coEvery { storage.getAuthHeader() } returns TEST_AUTH_HEADER
        assertThat(subject.getAuthHeader()).isEqualTo(TEST_AUTH_HEADER)
    }

    @Test
    fun `when authenticated successfully then stores token and url`() = runTest {
        coEvery { dataSource.authenticate(eq(TEST_USERNAME), eq(TEST_PASSWORD)) } returns TEST_TOKEN
        subject.authenticate(TEST_USERNAME, TEST_PASSWORD)
        coVerify { storage.storeAuthData(TEST_AUTH_HEADER) }
    }

    @Test
    fun `when logout then clearAuthData is called`() = runTest {
        subject.logout()
        coVerify { storage.clearAuthData() }
    }
}
