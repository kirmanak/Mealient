package gq.kirmanak.mealient.extensions

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

fun Fragment.executeOnceOnBackPressed(action: () -> Unit) {
    val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
    lifecycleScope.launch {
        onBackPressedDispatcher.backPressedFlow().first()
        action()
        onBackPressedDispatcher.onBackPressed() // Execute other callbacks now
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun OnBackPressedDispatcher.backPressedFlow(): Flow<Unit> = callbackFlow {
    val callback = addCallback { trySend(Unit) }
    awaitClose {
        callback.isEnabled = false
        callback.remove()
    }
}

inline fun <T> Fragment.collectWithViewLifecycle(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit,
) = viewLifecycleOwner.lifecycleScope.launch { flow.collect(collector) }
