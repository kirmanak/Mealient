package gq.kirmanak.mealient.extensions

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