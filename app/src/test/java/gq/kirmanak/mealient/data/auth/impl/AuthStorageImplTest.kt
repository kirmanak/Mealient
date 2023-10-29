package gq.kirmanak.mealient.data.auth.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl.Companion.AUTH_HEADER_KEY
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.HiltRobolectricTest
import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthStorageImplTest : HiltRobolectricTest() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    lateinit var subject: AuthStorage

    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        subject = AuthStorageImpl(sharedPreferences, logger)
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
}