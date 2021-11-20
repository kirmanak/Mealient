package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val recipeRepo: RecipeRepo
) : ViewModel() {
    init {
        Timber.v("constructor called")
    }

    suspend fun authenticate(username: String, password: String, baseUrl: String) {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        authRepo.authenticate(username, password, baseUrl)
    }

    fun authenticationStatuses(): Flow<Boolean> {
        Timber.v("authenticationStatuses() called")
        return authRepo.authenticationStatuses()
    }

    fun logout() {
        Timber.v("logout() called")
        authRepo.logout()
        viewModelScope.launch { recipeRepo.clearLocalData() }
    }
}