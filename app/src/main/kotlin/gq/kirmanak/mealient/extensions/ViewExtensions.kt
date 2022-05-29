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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
fun SwipeRefreshLayout.refreshRequestFlow(): Flow<Unit> = callbackFlow {
    Timber.v("refreshRequestFlow() called")
    val listener = SwipeRefreshLayout.OnRefreshListener {
        Timber.v("refreshRequestFlow: listener called")
        trySend(Unit).logErrors("refreshesFlow")
    }
    setOnRefreshListener(listener)
    awaitClose {
        Timber.v("Removing refresh request listener")
        setOnRefreshListener(null)
    }
}

fun Activity.setSystemUiVisibility(isVisible: Boolean) {
    Timber.v("setSystemUiVisibility() called with: isVisible = $isVisible")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) setSystemUiVisibilityV30(isVisible)
    else setSystemUiVisibilityV1(isVisible)
}

@Suppress("DEPRECATION")
private fun Activity.setSystemUiVisibilityV1(isVisible: Boolean) {
    Timber.v("setSystemUiVisibilityV1() called with: isVisible = $isVisible")
    window.decorView.systemUiVisibility = if (isVisible) 0 else View.SYSTEM_UI_FLAG_FULLSCREEN
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Activity.setSystemUiVisibilityV30(isVisible: Boolean) {
    Timber.v("setSystemUiVisibilityV30() called with: isVisible = $isVisible")
    val systemBars = WindowInsets.Type.systemBars()
    window.insetsController?.apply { if (isVisible) show(systemBars) else hide(systemBars) }
        ?: Timber.w("setSystemUiVisibilityV30: insets controller is null")
}

fun AppCompatActivity.setActionBarVisibility(isVisible: Boolean) {
    Timber.v("setActionBarVisibility() called with: isVisible = $isVisible")
    supportActionBar?.apply { if (isVisible) show() else hide() }
        ?: Timber.w("setActionBarVisibility: action bar is null")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun TextView.textChangesFlow(): Flow<CharSequence?> = callbackFlow {
    Timber.v("textChangesFlow() called")
    val textWatcher = doAfterTextChanged {
        trySend(it).logErrors("textChangesFlow")
    }
    awaitClose {
        Timber.d("textChangesFlow: flow is closing")
        removeTextChangedListener(textWatcher)
    }
}

fun <T> ChannelResult<T>.logErrors(methodName: String): ChannelResult<T> {
    onFailure { Timber.e(it, "$methodName: can't send event") }
    onClosed { Timber.e(it, "$methodName: flow has been closed") }
    return this
}

fun EditText.checkIfInputIsEmpty(
    inputLayout: TextInputLayout,
    lifecycleOwner: LifecycleOwner,
    @StringRes stringId: Int,
    trim: Boolean = true,
): String? {
    val input = if (trim) text?.trim() else text
    val text = input?.toString().orEmpty()
    Timber.d("Input text is \"$text\"")
    return text.ifEmpty {
        inputLayout.error = resources.getString(stringId)
        lifecycleOwner.lifecycleScope.launch {
            waitUntilNotEmpty()
            inputLayout.error = null
        }
        null
    }
}

suspend fun EditText.waitUntilNotEmpty() {
    textChangesFlow().filterNotNull().first { it.isNotEmpty() }
    Timber.v("waitUntilNotEmpty() returned")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> SharedPreferences.prefsChangeFlow(
    valueReader: SharedPreferences.() -> T,
): Flow<T> = callbackFlow {
    fun sendValue() = trySend(valueReader()).logErrors("prefsChangeFlow")
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> sendValue() }
    sendValue()
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}