package gq.kirmanak.mealient.extensions

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}


fun Context.showLongToast(text: String) = showToast(text, Toast.LENGTH_LONG)

fun Context.showLongToast(@StringRes text: Int) = showLongToast(getString(text))

private fun Context.showToast(text: String, length: Int) {
    Toast.makeText(this, text, length).show()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}