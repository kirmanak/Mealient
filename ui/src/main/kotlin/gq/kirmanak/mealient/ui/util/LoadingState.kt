package gq.kirmanak.mealient.ui.util

sealed class LoadingState<out T>

sealed class LoadingStateWithData<out T> : LoadingState<T>() {

    abstract val data: T

    data class Refreshing<T>(override val data: T) : LoadingStateWithData<T>()

    data class Success<T>(override val data: T) : LoadingStateWithData<T>()

}

sealed class LoadingStateNoData<out T> : LoadingState<T>() {

    object InitialLoad : LoadingStateNoData<Nothing>()

    data class LoadError<T>(val error: Throwable) : LoadingStateNoData<T>()
}

val <T> LoadingState<T>.isLoading: Boolean
    get() = when (this) {
        is LoadingStateNoData.LoadError,
        is LoadingStateWithData.Success -> false

        is LoadingStateNoData.InitialLoad,
        is LoadingStateWithData.Refreshing -> true
    }

val <T> LoadingState<T>.error: Throwable?
    get() = when (this) {
        is LoadingStateNoData.LoadError -> error
        is LoadingStateNoData.InitialLoad,
        is LoadingStateWithData.Refreshing,
        is LoadingStateWithData.Success -> null
    }

val <T> LoadingState<T>.data: T?
    get() = when (this) {
        is LoadingStateWithData -> data
        is LoadingStateNoData -> null
    }

val <T> LoadingState<T>.isRefreshing: Boolean
    get() = when (this) {
        is LoadingStateWithData.Refreshing -> true
        is LoadingStateWithData.Success,
        is LoadingStateNoData.InitialLoad,
        is LoadingStateNoData.LoadError -> false
    }

inline fun <T, E> LoadingState<T>.map(block: (T) -> E) = when (this) {
    is LoadingStateWithData.Success -> LoadingStateWithData.Success(block(data))
    is LoadingStateWithData.Refreshing -> LoadingStateWithData.Refreshing(block(data))
    is LoadingStateNoData.InitialLoad -> LoadingStateNoData.InitialLoad
    is LoadingStateNoData.LoadError -> LoadingStateNoData.LoadError(error)
}