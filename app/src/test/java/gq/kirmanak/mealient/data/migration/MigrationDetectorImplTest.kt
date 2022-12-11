package gq.kirmanak.mealient.data.migration

import androidx.datastore.preferences.core.intPreferencesKey
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MigrationDetectorImplTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var buildConfiguration: BuildConfiguration

    @MockK(relaxUnitFun = true)
    lateinit var preferencesStorage: PreferencesStorage

    @Test
    fun `when last version matches current expect no executors to be called`() = runTest {
        val executors = setOf<MigrationExecutor>(mockk(), mockk())
        val key = intPreferencesKey("key")
        every { preferencesStorage.lastExecutedMigrationVersionKey } returns intPreferencesKey("key")
        coEvery { preferencesStorage.getValue(key) } returns 25
        coEvery { buildConfiguration.versionCode() } returns 25
        buildSubject(executors).executeMigrations()
        executors.forEach {
            coVerify(inverse = true) { it.migratingFrom }
            coVerify(inverse = true) { it.executeMigration() }
        }
    }

    @Test
    fun `when last version is 24 and current is 25 expect all executors to be checked`() = runTest {
        val firstExecutor = mockk<MigrationExecutor>()
        every { firstExecutor.migratingFrom } returns 3

        val secondExecutor = mockk<MigrationExecutor>()
        every { secondExecutor.migratingFrom } returns 5

        val executors = setOf(firstExecutor, secondExecutor)

        val key = intPreferencesKey("key")
        every { preferencesStorage.lastExecutedMigrationVersionKey } returns intPreferencesKey("key")

        coEvery { preferencesStorage.getValue(key) } returns 24
        coEvery { buildConfiguration.versionCode() } returns 25

        buildSubject(executors).executeMigrations()
        executors.forEach {
            coVerify { it.migratingFrom }
            coVerify(inverse = true) { it.executeMigration() }
        }
    }

    @Test
    fun `when last version is 24 and current is 25 expect the executor to be called`() = runTest {
        val firstExecutor = mockk<MigrationExecutor>(relaxUnitFun = true)
        every { firstExecutor.migratingFrom } returns 24

        val executors = setOf(firstExecutor)

        val key = intPreferencesKey("key")
        every { preferencesStorage.lastExecutedMigrationVersionKey } returns intPreferencesKey("key")

        coEvery { preferencesStorage.getValue(key) } returns 24
        coEvery { buildConfiguration.versionCode() } returns 25

        buildSubject(executors).executeMigrations()
        executors.forEach {
            coVerify { it.migratingFrom }
            coVerify { it.executeMigration() }
        }
    }

    private fun buildSubject(executors: Set<MigrationExecutor>) = MigrationDetectorImpl(
        preferencesStorage = preferencesStorage,
        migrationExecutors = executors,
        buildConfiguration = buildConfiguration,
        logger = logger,
    )
}