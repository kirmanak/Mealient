package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
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
import gq.kirmanak.mealient.ui.recipes.list.RecipeListItemState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    authRepo: AuthRepo,
) : ViewModel() {

    val pagingData: Flow<PagingData<RecipeSummaryEntity>> = recipeRepo.createPager().flow
        .cachedIn(viewModelScope)

    val pagingDataRecipeState: Flow<PagingData<RecipeListItemState>> = pagingData.map { data ->
        data.map { item ->
            val imageUrl = recipeImageUrlProvider.generateImageUrl(item.imageId)
            RecipeListItemState(
                imageUrl = imageUrl,
                entity = item,
            )
        }
    }

    val showFavoriteIcon: StateFlow<Boolean> = authRepo.isAuthorizedFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _deleteRecipeResult = MutableSharedFlow<Result<Unit>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val deleteRecipeResult: SharedFlow<Result<Unit>> get() = _deleteRecipeResult

    init {
        authRepo.isAuthorizedFlow.valueUpdatesOnly().onEach { hasAuthorized ->
            logger.v { "Authorization state changed to $hasAuthorized" }
            if (hasAuthorized) recipeRepo.refreshRecipes()
        }.launchIn(viewModelScope)
    }

    fun refreshRecipeInfo(recipeSlug: String): LiveData<Result<Unit>> {
        logger.v { "refreshRecipeInfo called with: recipeSlug = $recipeSlug" }
        return liveData {
            val result = recipeRepo.refreshRecipeInfo(recipeSlug)
            logger.v { "refreshRecipeInfo: emitting $result" }
            emit(result)
        }
    }

    fun onFavoriteIconClick(recipeSummaryEntity: RecipeSummaryEntity) = liveData {
        logger.v { "onFavoriteIconClick() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        recipeRepo.updateIsRecipeFavorite(
            recipeSlug = recipeSummaryEntity.slug,
            isFavorite = recipeSummaryEntity.isFavorite.not(),
        ).also { emit(it) }
    }

    fun onDeleteConfirm(recipeSummaryEntity: RecipeSummaryEntity) {
        logger.v { "onDeleteConfirm() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        viewModelScope.launch {
            val result = recipeRepo.deleteRecipe(recipeSummaryEntity)
            logger.d { "onDeleteConfirm: delete result is $result" }
            _deleteRecipeResult.emit(result)
        }
    }
}