package gq.kirmanak.mealient.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.launchIn

fun <T> Fragment.collectWhenViewResumed(
    flow: Flow<T>,
    collector: FlowCollector<T>,
) = launchWhenViewResumed { flow.collect(collector) }

fun Fragment.launchWhenViewResumed(
    block: suspend CoroutineScope.() -> Unit,
) = viewLifecycleOwner.lifecycleScope.launchWhenResumed(block)

fun <T> Flow<T>.launchIn(lifecycleOwner: LifecycleOwner) {
    launchIn(lifecycleOwner.lifecycleScope)
}

fun Fragment.showLongToast(@StringRes text: Int) = showLongToast(getString(text))

fun Fragment.showLongToast(text: String) = showToast(text, Toast.LENGTH_LONG)

private fun Fragment.showToast(text: String, length: Int): Boolean {
    return context?.let { Toast.makeText(it, text, length).show() } != null
}
