package gq.kirmanak.mealient.data.storage

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesStorage {

    val baseUrlKey: Preferences.Key<String>

    val serverVersionKey: Preferences.Key<String>

    val isDisclaimerAcceptedKey: Preferences.Key<Boolean>

    val lastExecutedMigrationVersionKey: Preferences.Key<Int>

    suspend fun <T> getValue(key: Preferences.Key<T>): T?

    suspend fun <T> requireValue(key: Preferences.Key<T>): T

    suspend fun <T> storeValues(vararg pairs: Pair<Preferences.Key<T>, T>)

    fun <T> valueUpdates(key: Preferences.Key<T>): Flow<T?>

    suspend fun <T> removeValues(vararg keys: Preferences.Key<T>)
}