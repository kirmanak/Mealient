package gq.kirmanak.mealient.ui.util

import kotlinx.coroutines.flow.StateFlow

interface LoadingHelper<T> {

    val loadingState: StateFlow<LoadingState<T>>

    suspend fun refresh(): Result<T>
}