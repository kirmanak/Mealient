package gq.kirmanak.mealient.data.migration

import android.content.SharedPreferences
import androidx.core.content.edit
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.datastore.DataStoreModule.Companion.ENCRYPTED
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class From24AuthMigrationExecutor @Inject constructor(
    @Named(ENCRYPTED) private val sharedPreferences: SharedPreferences,
    private val authRepo: AuthRepo,
    private val logger: Logger,
) : MigrationExecutor {

    override val migratingFrom: Int = 24

    override suspend fun executeMigration() {
        logger.v { "executeMigration() was called" }
        val email = sharedPreferences.getString(EMAIL_KEY, null)
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        if (email != null && password != null) {
            runCatchingExceptCancel { authRepo.authenticate(email, password) }
                .onFailure { logger.e(it) { "API token creation failed" } }
                .onSuccess { logger.i { "Created API token during migration" } }
        }
        sharedPreferences.edit {
            remove(EMAIL_KEY)
            remove(PASSWORD_KEY)
        }
    }

    companion object {
        private const val EMAIL_KEY = "email"
        private const val PASSWORD_KEY = "password"
    }
}