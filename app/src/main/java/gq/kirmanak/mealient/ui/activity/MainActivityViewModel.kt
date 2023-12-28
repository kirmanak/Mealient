package gq.kirmanak.mealient.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.destinations.AuthenticationScreenDestination
import gq.kirmanak.mealient.ui.destinations.BaseURLScreenDestination
import gq.kirmanak.mealient.ui.destinations.DirectionDestination
import gq.kirmanak.mealient.ui.destinations.DisclaimerScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val logger: Logger,
    private val disclaimerStorage: DisclaimerStorage,
    private val serverInfoRepo: ServerInfoRepo,
    private val recipeRepo: RecipeRepo,
) : ViewModel() {

    private val _appState = MutableStateFlow(MealientAppState())
    val appState: StateFlow<MealientAppState> get() = _appState.asStateFlow()

    init {
        checkForcedDestination()
    }

    private fun checkForcedDestination() {
        val baseUrlSetState = serverInfoRepo.baseUrlFlow.map { it != null }
        val tokenExistsState = authRepo.isAuthorizedFlow
        val disclaimerAcceptedState = disclaimerStorage.isDisclaimerAcceptedFlow

        viewModelScope.launch {
            combine(
                baseUrlSetState,
                tokenExistsState,
                disclaimerAcceptedState,
            ) { baseUrlSet, tokenExists, disclaimerAccepted ->
                when {
                    !disclaimerAccepted -> ForcedDestination.Destination(DisclaimerScreenDestination)
                    !baseUrlSet -> ForcedDestination.Destination(BaseURLScreenDestination)
                    !tokenExists -> ForcedDestination.Destination(AuthenticationScreenDestination)
                    else -> ForcedDestination.None
                }
            }.collect { destination ->
                _appState.update { it.copy(forcedRoute = destination) }
            }
        }
    }

    fun onEvent(event: AppEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is AppEvent.Logout -> {
                viewModelScope.launch { authRepo.logout() }
            }

            is AppEvent.EmailLogs -> {
                // TODO
            }

            is AppEvent.SearchQueryChanged -> {
                _appState.update { it.copy(searchQuery = event.query) }
                recipeRepo.updateNameQuery(event.query)
            }
        }
    }
}

internal data class MealientAppState(
    val forcedRoute: ForcedDestination = ForcedDestination.Undefined,
    val searchQuery: String = "",
)

internal sealed interface ForcedDestination {

    /**
     * Force navigation is required
     */
    data class Destination(
        val route: DirectionDestination,
    ) : ForcedDestination

    /**
     * The conditions were checked, no force navigation required
     */
    data object None : ForcedDestination

    /**
     * The conditions were not checked yet
     */
    data object Undefined : ForcedDestination
}

internal sealed interface AppEvent {

    data object Logout : AppEvent

    data object EmailLogs : AppEvent

    data class SearchQueryChanged(val query: String) : AppEvent
}