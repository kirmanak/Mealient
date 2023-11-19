package gq.kirmanak.mealient.ui.util

import kotlinx.coroutines.CoroutineScope

interface LoadingHelperFactory {

    fun <T> create(
        coroutineScope: CoroutineScope,
        fetch: suspend () -> Result<T>,
    ): LoadingHelper<T>
}