package gq.kirmanak.mealient.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun <T> Fragment.collectWhenViewResumed(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.collectWhenResumed(flow, collector)
}

fun Fragment.showLongToast(@StringRes text: Int) = context?.showLongToast(text) != null

fun Fragment.showLongToast(text: String) = context?.showLongToast(text) != null

