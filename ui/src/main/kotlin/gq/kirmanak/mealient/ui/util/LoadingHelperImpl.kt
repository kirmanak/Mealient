package gq.kirmanak.mealient.ui.util

import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class LoadingHelperImpl<T>(
    private val logger: Logger,
    private val fetch: suspend () -> Result<T>,
) : LoadingHelper<T> {

    private val _loadingState = MutableStateFlow<LoadingState<T>>(LoadingStateNoData.InitialLoad)
    override val loadingState: StateFlow<LoadingState<T>> = _loadingState

    override suspend fun refresh(): Result<T> {
        logger.v { "refresh() called" }
        _loadingState.update { currentState ->
            when (currentState) {
                is LoadingStateWithData -> LoadingStateWithData.Refreshing(currentState.data)
                is LoadingStateNoData -> LoadingStateNoData.InitialLoad
            }
        }
        val result = fetch()
        _loadingState.update { currentState ->
            result.fold(
                onSuccess = { data ->
                    LoadingStateWithData.Success(data)
                },
                onFailure = { error ->
                    when (currentState) {
                        is LoadingStateWithData -> LoadingStateWithData.Success(currentState.data)
                        is LoadingStateNoData -> LoadingStateNoData.LoadError(error)
                    }
                },
            )
        }
        return result
    }

}