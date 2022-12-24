package gq.kirmanak.mealient.ui

import kotlinx.coroutines.flow.StateFlow

interface ActivityUiStateController {

    fun updateUiState(update: (ActivityUiState) -> ActivityUiState)

    fun getUiState(): ActivityUiState

    fun getUiStateFlow(): StateFlow<ActivityUiState>
}