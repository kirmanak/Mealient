package gq.kirmanak.mealie.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.auth.AuthRepo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {
    init {
        Timber.v("constructor called")
    }

    suspend fun isAuthenticated(): Boolean {
        Timber.v("isAuthenticated() called")
        val result = authRepo.isAuthenticated()
        Timber.d("isAuthenticated() returned: $result")
        return result
    }

    suspend fun authenticate(username: String, password: String, baseUrl: String): Throwable? {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val result = authRepo.authenticate(username, password, baseUrl)
        if (result == null) Timber.d("authenticate() returns null")
        else Timber.e("authenticate() returns error", result)
        return result
    }
}