package gq.kirmanak.mealient.data.auth.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl.Companion.AUTH_HEADER_KEY
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl.Companion.EMAIL_KEY
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl.Companion.PASSWORD_KEY
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AuthStorageImplTest : HiltRobolectricTest() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    lateinit var subject: AuthStorage

    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        subject = AuthStorageImpl(sharedPreferences)
    }

    @Test
    fun `when authHeaderFlow is observed then sends value immediately`() = runTest {
        sharedPreferences.edit(commit = true) { putString(AUTH_HEADER_KEY, TEST_AUTH_HEADER) }
        assertThat(subject.authHeaderFlow.first()).isEqualTo(TEST_AUTH_HEADER)
    }

    @Test
    fun `when authHeader is observed then sends null if nothing saved`() = runTest {
        assertThat(subject.authHeaderFlow.first()).isEqualTo(null)
    }

    @Test
    fun `when setEmail then edits shared preferences`() = runTest {
        subject.setEmail(TEST_USERNAME)
        assertThat(sharedPreferences.getString(EMAIL_KEY, null)).isEqualTo(TEST_USERNAME)
    }

    @Test
    fun `when getPassword then reads shared preferences`() = runTest {
        sharedPreferences.edit(commit = true) { putString(PASSWORD_KEY, TEST_PASSWORD) }
        assertThat(subject.getPassword()).isEqualTo(TEST_PASSWORD)
    }
}