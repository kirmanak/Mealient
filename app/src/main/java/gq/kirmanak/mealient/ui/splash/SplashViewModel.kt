package gq.kirmanak.mealient.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val disclaimerStorage: DisclaimerStorage,
    private val baseURLStorage: BaseURLStorage,
) : ViewModel() {
    private val _nextDestination = MutableLiveData<NavDirections>()
    val nextDestination: LiveData<NavDirections> = _nextDestination

    init {
        viewModelScope.launch {
            delay(1000)
            _nextDestination.value = when {
                !disclaimerStorage.isDisclaimerAccepted() -> SplashFragmentDirections.actionSplashFragmentToDisclaimerFragment()
                baseURLStorage.getBaseURL() == null -> SplashFragmentDirections.actionSplashFragmentToBaseURLFragment()
                else -> SplashFragmentDirections.actionSplashFragmentToRecipesFragment()
            }
        }
    }
}
