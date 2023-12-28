package gq.kirmanak.mealient.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <T> ChannelResult<T>.logErrors(methodName: String, logger: Logger): ChannelResult<T> {
    onFailure { logger.e(it) { "$methodName: can't send event" } }
    onClosed { logger.e(it) { "$methodName: flow has been closed" } }
    return this
}

fun <T> SharedPreferences.prefsChangeFlow(
    logger: Logger,
    valueReader: SharedPreferences.() -> T,
): Flow<T> = callbackFlow {
    fun sendValue() = trySend(valueReader()).logErrors("prefsChangeFlow", logger)
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> sendValue() }
    sendValue()
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}

fun Context.showLongToast(text: String) = showToast(text, Toast.LENGTH_LONG)

fun Context.showLongToast(@StringRes text: Int) = showLongToast(getString(text))

private fun Context.showToast(text: String, length: Int) {
    Toast.makeText(this, text, length).show()
}

fun Context.isDarkThemeOn(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) resources.configuration.isNightModeActive
    else resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

