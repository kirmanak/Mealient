package gq.kirmanak.mealient.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
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

fun TextView.textChangesLiveData(logger: Logger): LiveData<CharSequence?> = callbackFlow {
    logger.v { "textChangesLiveData() called" }
    val textWatcher = doAfterTextChanged {
        trySend(it).logErrors("textChangesFlow", logger)
    }
    awaitClose {
        logger.d { "textChangesLiveData: flow is closing" }
        removeTextChangedListener(textWatcher)
    }
}.asLiveData() // Use asLiveData() to make sure close() is called with a delay to avoid IndexOutOfBoundsException

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
        val textChangesLiveData = textChangesLiveData(logger)
        textChangesLiveData.observe(lifecycleOwner, object : Observer<CharSequence?> {
            override fun onChanged(value: CharSequence?) {
                if (value.isNullOrBlank().not()) {
                    inputLayout.error = null
                    textChangesLiveData.removeObserver(this)
                }
            }
        })
        null
    }
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

fun Context.isDarkThemeOn(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) resources.configuration.isNightModeActive
    else resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun <T> LifecycleOwner.collectWhenResumed(flow: Flow<T>, collector: FlowCollector<T>) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            flow.collect(collector)
        }
    }
}

val <T : ViewBinding> T.resources: Resources
    get() = root.resources