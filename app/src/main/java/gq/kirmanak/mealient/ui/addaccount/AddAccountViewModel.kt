package gq.kirmanak.mealient.ui.addaccount

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import timber.log.Timber

@HiltViewModel
class AddAccountViewModel(
    private val authRepo: AuthRepo,
) : ViewModel() {

    suspend fun authenticate(username: String, password: String) = runCatchingExceptCancel {
        Timber.v("authenticate() called with: username = $username, password = $password")
        authRepo.authenticate(username, password)
    }.onFailure {
        Timber.e(it, "authenticate: can't authenticate")
    }
}