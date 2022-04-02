package gq.kirmanak.mealient.data.impl.util

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun SharedPreferences.getStringOrNull(key: String) =
    withContext(Dispatchers.IO) { getString(key, null) }

suspend fun SharedPreferences.getBooleanOrFalse(key: String) =
    withContext(Dispatchers.IO) { getBoolean(key, false) }

@OptIn(ExperimentalCoroutinesApi::class)
fun SharedPreferences.changesFlow(): Flow<Pair<SharedPreferences, String?>> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        Timber.v("watchChanges: listener called with key $key")
        trySend(prefs to key)
            .onFailure { Timber.e(it, "watchChanges: can't send preference change, key $key") }
            .onClosed { Timber.e(it, "watchChanges: flow has been closed") }
    }
    Timber.v("watchChanges: registering listener")
    registerOnSharedPreferenceChangeListener(listener)
    send(this@changesFlow to null)
    awaitClose {
        Timber.v("watchChanges: flow has been closed")
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}

