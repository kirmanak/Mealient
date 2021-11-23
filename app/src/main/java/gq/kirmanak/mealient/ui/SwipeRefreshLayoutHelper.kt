package gq.kirmanak.mealient.ui

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@ExperimentalCoroutinesApi
object SwipeRefreshLayoutHelper {
    private fun SwipeRefreshLayout.refreshesFlow(): Flow<Unit> {
        Timber.v("refreshesFlow() called")
        return callbackFlow {
            val listener = SwipeRefreshLayout.OnRefreshListener {
                Timber.v("Refresh requested")
                trySend(Unit).onFailure { Timber.e(it, "Can't send refresh callback") }
            }
            Timber.v("Adding refresh request listener")
            setOnRefreshListener(listener)
            awaitClose {
                Timber.v("Removing refresh request listener")
                setOnRefreshListener(null)
            }
        }
    }

    suspend fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.listenToRefreshRequests(
        refreshLayout: SwipeRefreshLayout
    ) {
        Timber.v("listenToRefreshRequests() called")
        refreshLayout.refreshesFlow().collectLatest {
            Timber.d("Received refresh request")
            refresh()
        }
    }
}