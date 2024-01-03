package gq.kirmanak.mealient.ui.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecipesListViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    authRepo: AuthRepo,
) : ViewModel() {

    private val pagingData: Flow<PagingData<RecipeSummaryEntity>> =
        recipeRepo.createPager().flow.cachedIn(viewModelScope)

    private val showFavoriteIcon: StateFlow<Boolean> =
        authRepo.isAuthorizedFlow.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val pagingDataRecipeState: Flow<PagingData<RecipeListItemState>> =
        pagingData.combine(showFavoriteIcon) { data, showFavorite ->
            data.map { item ->
                val imageUrl = recipeImageUrlProvider.generateImageUrl(item.imageId)
                RecipeListItemState(
                    imageUrl = imageUrl,
                    showFavoriteIcon = showFavorite,
                    entity = item,
                )
            }
        }

    private val _screenState = MutableStateFlow(
        RecipeListState(pagingDataRecipeState = pagingDataRecipeState)
    )
    val screenState: StateFlow<RecipeListState> get() = _screenState.asStateFlow()

    init {
        authRepo.isAuthorizedFlow.valueUpdatesOnly().onEach { hasAuthorized ->
            logger.v { "Authorization state changed to $hasAuthorized" }
            if (hasAuthorized) recipeRepo.refreshRecipes()
        }.launchIn(viewModelScope)
    }

    private fun onRecipeClicked(entity: RecipeSummaryEntity) {
        logger.v { "onRecipeClicked() called with: entity = $entity" }
        viewModelScope.launch {
            val result = recipeRepo.refreshRecipeInfo(entity.slug)
            logger.d { "Recipe info refreshed: $result" }
            _screenState.update { it.copy(recipeIdToOpen = entity.remoteId) }
        }
    }

    private fun onFavoriteIconClick(recipeSummaryEntity: RecipeSummaryEntity) {
        logger.v { "onFavoriteIconClick() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        viewModelScope.launch {
            val result = recipeRepo.updateIsRecipeFavorite(
                recipeSlug = recipeSummaryEntity.slug,
                isFavorite = recipeSummaryEntity.isFavorite.not(),
            )
            val snackbar = result.fold(
                onSuccess = { isFavorite ->
                    val name = recipeSummaryEntity.name
                    if (isFavorite) {
                        RecipeListSnackbar.FavoriteAdded(name)
                    } else {
                        RecipeListSnackbar.FavoriteRemoved(name)
                    }
                },
                onFailure = {
                    RecipeListSnackbar.FavoriteUpdateFailed
                }
            )
            _screenState.update { it.copy(snackbarState = snackbar) }
        }
    }

    private fun onDeleteConfirm(recipeSummaryEntity: RecipeSummaryEntity) {
        logger.v { "onDeleteConfirm() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        viewModelScope.launch {
            val result = recipeRepo.deleteRecipe(recipeSummaryEntity)
            logger.d { "onDeleteConfirm: delete result is $result" }
            val snackbar = result.fold(
                onSuccess = { null },
                onFailure = { RecipeListSnackbar.DeleteFailed },
            )
            _screenState.update { it.copy(snackbarState = snackbar) }
        }
    }

    private fun onSnackbarShown() {
        _screenState.update { it.copy(snackbarState = null) }
    }

    private fun onRecipeOpen() {
        logger.v { "onRecipeOpen() called" }
        _screenState.update { it.copy(recipeIdToOpen = null) }
    }

    fun onEvent(event: RecipeListEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is RecipeListEvent.DeleteConfirmed -> onDeleteConfirm(event.recipe.entity)
            is RecipeListEvent.FavoriteClick -> onFavoriteIconClick(event.recipe.entity)
            is RecipeListEvent.RecipeClick -> onRecipeClicked(event.recipe.entity)
            is RecipeListEvent.SnackbarShown -> onSnackbarShown()
            is RecipeListEvent.RecipeOpened -> onRecipeOpen()
            is RecipeListEvent.SearchQueryChanged -> onSearchQueryChanged(event)
        }
    }

    private fun onSearchQueryChanged(event: RecipeListEvent.SearchQueryChanged) {
        logger.v { "onSearchQueryChanged() called with: event = $event" }
        _screenState.update { it.copy(searchQuery = event.query) }
        recipeRepo.updateNameQuery(event.query)
    }
}

internal data class RecipeListState(
    val pagingDataRecipeState: Flow<PagingData<RecipeListItemState>>,
    val snackbarState: RecipeListSnackbar? = null,
    val recipeIdToOpen: String? = null,
    val searchQuery: String = "",
)

internal sealed interface RecipeListEvent {

    data class DeleteConfirmed(val recipe: RecipeListItemState) : RecipeListEvent

    data class FavoriteClick(val recipe: RecipeListItemState) : RecipeListEvent

    data class RecipeClick(val recipe: RecipeListItemState) : RecipeListEvent

    data object RecipeOpened : RecipeListEvent

    data object SnackbarShown : RecipeListEvent

    data class SearchQueryChanged(val query: String) : RecipeListEvent
}