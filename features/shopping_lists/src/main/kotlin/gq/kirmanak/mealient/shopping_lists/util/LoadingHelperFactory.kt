package gq.kirmanak.mealient.shopping_lists.util

import kotlinx.coroutines.CoroutineScope

interface LoadingHelperFactory {

    fun <T> create(coroutineScope: CoroutineScope, fetch: suspend () -> T): LoadingHelper<T>
}