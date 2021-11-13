package gq.kirmanak.mealie.data.auth.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_URL
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthStorageImplTest : HiltRobolectricTest() {
    @Inject
    lateinit var subject: AuthStorageImpl

    @Test
    fun `when storing auth data then doesn't throw`() = runBlocking {
        subject.storeAuthData(TEST_TOKEN, TEST_URL)
    }

    @Test
    fun `when reading url after storing data then returns url`() = runBlocking {
        subject.storeAuthData(TEST_TOKEN, TEST_URL)
        assertThat(subject.getBaseUrl()).isEqualTo(TEST_URL)
    }

    @Test
    fun `when reading token after storing data then returns token`() = runBlocking {
        subject.storeAuthData(TEST_TOKEN, TEST_URL)
        assertThat(subject.getToken()).isEqualTo(TEST_TOKEN)
    }

    @Test
    fun `when reading token without storing data then returns null`() = runBlocking {
        assertThat(subject.getToken()).isNull()
    }

    @Test
    fun `when reading url without storing data then returns null`() = runBlocking {
        assertThat(subject.getBaseUrl()).isNull()
    }
}