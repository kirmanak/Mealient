package gq.kirmanak.mealient.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Fragment.collectWhenViewResumed(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            flow.collect(collector)
        }
    }
}

fun Fragment.showLongToast(@StringRes text: Int) = showLongToast(getString(text))

fun Fragment.showLongToast(text: String) = showToast(text, Toast.LENGTH_LONG)

private fun Fragment.showToast(text: String, length: Int): Boolean {
    return context?.let { Toast.makeText(it, text, length).show() } != null
}
