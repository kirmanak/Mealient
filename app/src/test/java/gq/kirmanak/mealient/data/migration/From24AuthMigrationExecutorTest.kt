package gq.kirmanak.mealient.data.migration

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.test.HiltRobolectricTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class From24AuthMigrationExecutorTest : HiltRobolectricTest() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    private lateinit var subject: MigrationExecutor
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        subject = From24AuthMigrationExecutor(sharedPreferences, authRepo, logger)
    }

    @Test
    fun `when there were email and password expect authentication`() = runTest {
        sharedPreferences.edit(commit = true) {
            putString("email", "email_value")
            putString("password", "pass_value")
        }
        subject.executeMigration()
        coVerify { authRepo.authenticate(eq("email_value"), eq("pass_value")) }
    }

    @Test
    fun `when there were email and password expect them gone`() = runTest {
        sharedPreferences.edit(commit = true) {
            putString("email", "email_value")
            putString("password", "pass_value")
        }
        subject.executeMigration()
        assertThat(sharedPreferences.getString("email", null)).isNull()
        assertThat(sharedPreferences.getString("password", null)).isNull()
    }

    @Test
    fun `when there is email and password but authenticate fails expect values gone`() = runTest {
        sharedPreferences.edit(commit = true) {
            putString("email", "email_value")
            putString("password", "pass_value")
        }
        coEvery { authRepo.authenticate(any(), any()) } throws IOException()
        subject.executeMigration()
        assertThat(sharedPreferences.getString("email", null)).isNull()
        assertThat(sharedPreferences.getString("password", null)).isNull()
    }

    @Test
    fun `when there was no email and password expect no authentication`() = runTest {
        subject.executeMigration()
        coVerify(inverse = true) { authRepo.authenticate(any(), any()) }
    }
}