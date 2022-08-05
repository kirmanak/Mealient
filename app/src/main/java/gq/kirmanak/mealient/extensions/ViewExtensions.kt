package gq.kirmanak.mealient.extensions

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

fun SwipeRefreshLayout.refreshRequestFlow(logger: Logger): Flow<Unit> = callbackFlow {
    logger.v { "refreshRequestFlow() called" }
    val listener = SwipeRefreshLayout.OnRefreshListener {
        logger.v { "refreshRequestFlow: listener called" }
        trySend(Unit).logErrors("refreshesFlow", logger)
    }
    setOnRefreshListener(listener)
    awaitClose {
        logger.v { "Removing refresh request listener" }
        setOnRefreshListener(null)
    }
}

fun Activity.setSystemUiVisibility(isVisible: Boolean, logger: Logger) {
    logger.v { "setSystemUiVisibility() called with: isVisible = $isVisible" }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) setSystemUiVisibilityV30(isVisible, logger)
    else setSystemUiVisibilityV1(isVisible, logger)
}

@Suppress("DEPRECATION")
private fun Activity.setSystemUiVisibilityV1(isVisible: Boolean, logger: Logger) {
    logger.v { "setSystemUiVisibilityV1() called with: isVisible = $isVisible" }
    window.decorView.systemUiVisibility = if (isVisible) 0 else View.SYSTEM_UI_FLAG_FULLSCREEN
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Activity.setSystemUiVisibilityV30(isVisible: Boolean, logger: Logger) {
    logger.v { "setSystemUiVisibilityV30() called with: isVisible = $isVisible" }
    val systemBars = WindowInsets.Type.systemBars()
    window.insetsController?.apply { if (isVisible) show(systemBars) else hide(systemBars) }
        ?: logger.w { "setSystemUiVisibilityV30: insets controller is null" }
}

fun AppCompatActivity.setActionBarVisibility(isVisible: Boolean, logger: Logger) {
    logger.v { "setActionBarVisibility() called with: isVisible = $isVisible" }
    supportActionBar?.apply { if (isVisible) show() else hide() }
        ?: logger.w { "setActionBarVisibility: action bar is null" }
}

fun TextView.textChangesFlow(logger: Logger): Flow<CharSequence?> = callbackFlow {
    logger.v { "textChangesFlow() called" }
    val textWatcher = doAfterTextChanged {
        trySend(it).logErrors("textChangesFlow", logger)
    }
    awaitClose {
        logger.d { "textChangesFlow: flow is closing" }
        removeTextChangedListener(textWatcher)
    }
}

fun <T> ChannelResult<T>.logErrors(methodName: String, logger: Logger): ChannelResult<T> {
    onFailure { logger.e(it) { "$methodName: can't send event" } }
    onClosed { logger.e(it) { "$methodName: flow has been closed" } }
    return this
}

fun EditText.checkIfInputIsEmpty(
    inputLayout: TextInputLayout,
    lifecycleOwner: LifecycleOwner,
    @StringRes stringId: Int,
    trim: Boolean = true,
    logger: Logger,
): String? {
    val input = if (trim) text?.trim() else text
    val text = input?.toString().orEmpty()
    logger.d { "Input text is \"$text\"" }
    return text.ifEmpty {
        inputLayout.error = resources.getString(stringId)
        lifecycleOwner.lifecycleScope.launch {
            waitUntilNotEmpty(logger)
            inputLayout.error = null
        }
        null
    }
}

suspend fun EditText.waitUntilNotEmpty(logger: Logger) {
    textChangesFlow(logger).filterNotNull().first { it.isNotEmpty() }
    logger.v { "waitUntilNotEmpty() returned" }
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