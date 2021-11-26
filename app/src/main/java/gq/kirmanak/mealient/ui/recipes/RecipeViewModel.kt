package gq.kirmanak.mealient.ui.recipes

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeImageLoader
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader
) : ViewModel() {
    private var _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _nextRecipeInfoChannel = Channel<RecipeSummaryEntity>()
    val nextRecipeInfo: Flow<RecipeSummaryEntity> =
        _nextRecipeInfoChannel.receiveAsFlow()

    val adapter = RecipesPagingAdapter(this) {
        Timber.d("onClick: recipe clicked $it")
        viewModelScope.launch { _nextRecipeInfoChannel.send(it) }
    }

    init {
        setupAdapter()
    }

    fun loadRecipeImage(view: ImageView, recipeSummary: RecipeSummaryEntity?) {
        Timber.v("loadRecipeImage() called with: view = $view, recipeSummary = $recipeSummary")
        viewModelScope.launch {
            recipeImageLoader.loadRecipeImage(view, recipeSummary?.slug)
        }
    }

    private fun setupAdapter() {
        with(viewModelScope) {
            launch {
                recipeRepo.createPager().flow.cachedIn(this).collect {
                    Timber.d("setupAdapter: received data update")
                    adapter.submitData(it)
                }
            }
            launch {
                adapter.onPagesUpdatedFlow.collect {
                    Timber.d("setupAdapter: pages have been updated")
                    _isRefreshing.value = false
                }
            }
        }
    }
}