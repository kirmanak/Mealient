package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.datasource.mapToNetworkError
import gq.kirmanak.mealient.logging.Logger

inline fun <T> Logger.logAndMapErrors(
    block: () -> T,
    noinline logProvider: () -> String
): T = runCatchingExceptCancel(block).getOrElse {
    e(it, messageSupplier = logProvider)
    throw it.mapToNetworkError()
}