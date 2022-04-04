package gq.kirmanak.mealient.data.auth.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AuthStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: AuthStorageImpl

    @Test
    fun `when storing auth data then doesn't throw`() = runTest {
        subject.storeAuthData(TEST_AUTH_HEADER)
    }

    @Test
    fun `when reading token after storing data then returns token`() = runTest {
        subject.storeAuthData(TEST_AUTH_HEADER)
        assertThat(subject.getAuthHeader()).isEqualTo(TEST_AUTH_HEADER)
    }

    @Test
    fun `when reading token without storing data then returns null`() = runTest {
        assertThat(subject.getAuthHeader()).isNull()
    }

    @Test
    fun `when didn't store auth data then first token is null`() = runTest {
        assertThat(subject.authHeaderFlow.first()).isNull()
    }

    @Test
    fun `when stored auth data then first token is correct`() = runTest {
        subject.storeAuthData(TEST_AUTH_HEADER)
        assertThat(subject.authHeaderFlow.first()).isEqualTo(TEST_AUTH_HEADER)
    }

    @Test
    fun `when clearAuthData then first token is null`() = runTest {
        subject.storeAuthData(TEST_AUTH_HEADER)
        subject.clearAuthData()
        assertThat(subject.authHeaderFlow.first()).isNull()
    }

    @Test
    fun `when clearAuthData then getToken returns null`() = runTest {
        subject.storeAuthData(TEST_AUTH_HEADER)
        subject.clearAuthData()
        assertThat(subject.getAuthHeader()).isNull()
    }
}