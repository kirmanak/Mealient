package gq.kirmanak.mealient.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val logger: Logger,
) : PreferencesStorage {

    override val baseUrlKey = stringPreferencesKey("baseUrl")

    override val isDisclaimerAcceptedKey = booleanPreferencesKey("isDisclaimedAccepted")

    override suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        val value = dataStore.data.first()[key]
        logger.v { "getValue() returned: $value for $key" }
        return value
    }

    override suspend fun <T> requireValue(key: Preferences.Key<T>): T =
        checkNotNull(getValue(key)) { "Value at $key is null when it was required" }

    override suspend fun <T> storeValues(vararg pairs: Pair<Preferences.Key<T>, T>) {
        logger.v { "storeValues() called with: pairs = ${pairs.contentToString()}" }
        dataStore.edit { preferences ->
            pairs.forEach { preferences += it.toPreferencesPair() }
        }
    }

    override fun <T> valueUpdates(key: Preferences.Key<T>): Flow<T?> {
        logger.v { "valueUpdates() called with: key = $key" }
        return dataStore.data
            .map { it[key] }
            .distinctUntilChanged()
            .onEach { logger.d { "valueUpdates: new value at $key is $it" } }
            .onCompletion { logger.i(it) { "valueUpdates: finished" } }
    }

    override suspend fun <T> removeValues(vararg keys: Preferences.Key<T>) {
        logger.v { "removeValues() called with: key = ${keys.contentToString()}" }
        dataStore.edit { preferences ->
            keys.forEach { preferences -= it }
        }
    }
}

private fun <T> Pair<Preferences.Key<T>, T>.toPreferencesPair(): Preferences.Pair<T> {
    val (key, value) = this
    return key to value
}
