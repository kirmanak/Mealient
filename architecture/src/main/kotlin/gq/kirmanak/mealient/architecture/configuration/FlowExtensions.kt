package gq.kirmanak.mealient.architecture.configuration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun <T> Flow<T>.valueUpdatesOnly(): Flow<T> = when (this) {
    is ValueUpdateOnlyFlowImpl<T> -> this
    else -> ValueUpdateOnlyFlowImpl(this)
}

private class ValueUpdateOnlyFlowImpl<T>(private val upstream: Flow<T>) : Flow<T> {

    override suspend fun collect(collector: FlowCollector<T>) {
        var previousValue: T? = null
        upstream.collect { value ->
            if (previousValue != null && previousValue != value) {
                collector.emit(value)
            }
            previousValue = value
        }
    }

}