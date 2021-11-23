package gq.kirmanak.mealient.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val disclaimerStorage: DisclaimerStorage
) : ViewModel() {
    private val _nextDestination = MutableLiveData<NavDirections>()
    val nextDestination: LiveData<NavDirections> = _nextDestination

    init {
        viewModelScope.launch {
            delay(1000)
            _nextDestination.value = if (!disclaimerStorage.isDisclaimerAccepted())
                SplashFragmentDirections.actionSplashFragmentToDisclaimerFragment()
            else if (!authRepo.authenticationStatuses().first())
                SplashFragmentDirections.actionSplashFragmentToAuthenticationFragment()
            else
                SplashFragmentDirections.actionSplashFragmentToRecipesFragment()
        }
    }
}
