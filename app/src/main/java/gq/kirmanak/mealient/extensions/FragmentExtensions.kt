package gq.kirmanak.mealient.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

inline fun <T> Fragment.collectWhenViewResumed(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit,
) = launchWhenViewResumed { flow.collect(collector) }

fun Fragment.launchWhenViewResumed(
    block: suspend CoroutineScope.() -> Unit,
) = viewLifecycleOwner.lifecycleScope.launchWhenResumed(block)