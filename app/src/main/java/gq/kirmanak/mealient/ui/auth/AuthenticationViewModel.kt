package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val recipeRepo: RecipeRepo
) : ViewModel() {

    fun authenticate(username: String, password: String): LiveData<Result<Unit>> {
        Timber.v("authenticate() called with: username = $username, password = $password")
        val result = MutableLiveData<Result<Unit>>()
        viewModelScope.launch {
            runCatching {
                authRepo.authenticate(username, password)
            }.onFailure {
                Timber.e(it, "authenticate: can't authenticate")
                result.value = Result.failure(it)
            }.onSuccess {
                Timber.d("authenticate: authenticated")
                result.value = Result.success(Unit)
            }
        }
        return result
    }

    fun authenticationStatuses(): LiveData<Boolean> {
        Timber.v("authenticationStatuses() called")
        return authRepo.authenticationStatuses().asLiveData()
    }

    fun logout() {
        Timber.v("logout() called")
        viewModelScope.launch {
            authRepo.logout()
            recipeRepo.clearLocalData()
        }
    }
}