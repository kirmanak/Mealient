package gq.kirmanak.mealient.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

@ExperimentalCoroutinesApi
fun SwipeRefreshLayout.refreshesLiveData(): LiveData<Unit> {
    val callbackFlow: Flow<Unit> = callbackFlow {
        val listener = SwipeRefreshLayout.OnRefreshListener {
            Timber.v("Refresh requested")
            trySend(Unit)
                .onFailure { Timber.e(it, "refreshesFlow: can't send refresh callback") }
                .onClosed { Timber.e(it, "refreshesFlow: flow has been closed") }
        }
        Timber.v("Adding refresh request listener")
        setOnRefreshListener(listener)
        awaitClose {
            Timber.v("Removing refresh request listener")
            setOnRefreshListener(null)
        }
    }
    return callbackFlow.asLiveData()
}