package gq.kirmanak.mealie.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.auth.AuthRepo
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {
    suspend fun isAuthenticated(): Boolean = authRepo.isAuthenticated()

    suspend fun authenticate(username: String, password: String, baseUrl: String): Throwable? {
        return authRepo.authenticate(username, password, baseUrl)
    }
}