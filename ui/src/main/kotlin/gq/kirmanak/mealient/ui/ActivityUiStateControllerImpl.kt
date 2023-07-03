package gq.kirmanak.mealient.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityUiStateControllerImpl @Inject constructor() : ActivityUiStateController {
    private val uiState = MutableStateFlow(ActivityUiState())

    override fun updateUiState(update: (ActivityUiState) -> ActivityUiState) {
        uiState.getAndUpdate(update)
    }

    override fun getUiState(): ActivityUiState = uiState.value

    override fun getUiStateFlow(): StateFlow<ActivityUiState> = uiState.asStateFlow()
}