package gq.kirmanak.mealient.shopping_lists.util

import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadingHelperImpl<T>(
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
    private val fetch: suspend () -> T,
) : LoadingHelper<T> {

    private val _loadingJob = MutableStateFlow<Job?>(null)
    private val _loadingState = MutableStateFlow<LoadingState<T>>(LoadingStateNoData.InitialLoad)
    override val loadingState: StateFlow<LoadingState<T>> = _loadingState

    override fun refresh() {
        logger.v { "refresh() called" }

        val job = coroutineScope.launch(start = CoroutineStart.LAZY) {
            doRefresh()
        }

        if (!_loadingJob.compareAndSet(null, job)) {
            logger.d { "refresh() called while loading job is already running" }
            return
        }

        job.invokeOnCompletion {
            if (_loadingJob.compareAndSet(job, null)) {
                logger.d { "Loading job completed" }
            } else {
                logger.d { "Loading job completed but was already replaced" }
            }
        }

        job.start()
    }

    private suspend fun doRefresh() {
        _loadingState.update { currentState ->
            when (currentState) {
                is LoadingStateWithData -> LoadingStateWithData.Refreshing(currentState.data)
                is LoadingStateNoData -> LoadingStateNoData.InitialLoad
            }
        }

        val result = runCatching { fetch() }

        _loadingState.update { currentState ->
            result.fold(
                onSuccess = { data ->
                    LoadingStateWithData.Success(data)
                },
                onFailure = { error ->
                    when (currentState) {
                        is LoadingStateWithData -> {
                            LoadingStateWithData.RefreshError(currentState.data, error)
                        }

                        is LoadingStateNoData -> {
                            LoadingStateNoData.LoadError(error)
                        }
                    }
                },
            )
        }
    }
}