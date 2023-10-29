package gq.kirmanak.mealient.data.migration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class From30MigrationExecutor @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : MigrationExecutor {

    override val migratingFrom: Int = 30

    override suspend fun executeMigration() {
        dataStore.edit { prefs ->
            prefs -= stringPreferencesKey("serverVersion")
        }
    }
}