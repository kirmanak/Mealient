package gq.kirmanak.mealient.data.migration

import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MigrationDetectorImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
    private val migrationExecutors: Set<@JvmSuppressWildcards MigrationExecutor>,
    private val buildConfiguration: BuildConfiguration,
    private val logger: Logger,
) : MigrationDetector {


    override suspend fun executeMigrations() {
        val key = preferencesStorage.lastExecutedMigrationVersionKey

        val lastVersion = preferencesStorage.getValue(key) ?: VERSION_BEFORE_MIGRATION_IMPLEMENTED
        val currentVersion = buildConfiguration.versionCode()
        logger.i { "Last migration version is $lastVersion, current is $currentVersion" }

        if (lastVersion != currentVersion) {
            migrationExecutors
                .filter { it.migratingFrom <= lastVersion }
                .forEach {
                    runCatchingExceptCancel { it.executeMigration() }
                        .onFailure { logger.e(it) { "Migration executor failed: $it" } }
                        .onSuccess { logger.i { "Migration executor succeeded: $it" } }
                }
        }

        preferencesStorage.storeValues(Pair(key, currentVersion))
    }


    companion object {
        private const val VERSION_BEFORE_MIGRATION_IMPLEMENTED = 24
    }
}