package gq.kirmanak.mealient.data.migration

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import gq.kirmanak.mealient.datastore.DataStoreModule
import javax.inject.Inject
import javax.inject.Named

class From30MigrationExecutor @Inject constructor(
    @Named(DataStoreModule.ENCRYPTED) private val sharedPreferences: SharedPreferences,
    private val dataStore: DataStore<Preferences>,
) : MigrationExecutor {

    override val migratingFrom: Int = 30

    override suspend fun executeMigration() {
        dataStore.edit { prefs ->
            prefs -= stringPreferencesKey("serverVersion")
        }
        val authHeader = sharedPreferences.getString("authHeader", null)
        if (authHeader != null) {
            sharedPreferences.edit {
                val authToken = authHeader.removePrefix("Bearer ")
                putString("authToken", authToken)
            }
        }
    }
}