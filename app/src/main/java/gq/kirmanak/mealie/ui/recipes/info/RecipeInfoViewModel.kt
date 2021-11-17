package gq.kirmanak.mealie.ui.recipes.info

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.recipes.RecipeImageLoader
import gq.kirmanak.mealie.data.recipes.RecipeRepo
import gq.kirmanak.mealie.data.recipes.impl.FullRecipeInfo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader
) : ViewModel() {
    private val _recipeInfo = MutableLiveData<FullRecipeInfo>()
    val recipeInfo: LiveData<FullRecipeInfo> = _recipeInfo

    fun loadRecipeImage(view: ImageView, recipeSlug: String) {
        viewModelScope.launch {
            recipeImageLoader.loadRecipeImage(view, recipeSlug)
        }
    }

    fun loadRecipeInfo(recipeId: Long, recipeSlug: String) {
        Timber.v("loadRecipeInfo() called with: recipeId = $recipeId, recipeSlug = $recipeSlug")
        viewModelScope.launch {
            runCatching {
                recipeRepo.loadRecipeInfo(recipeId, recipeSlug)
            }.onSuccess {
                Timber.d("loadRecipeInfo: received recipe info = $it")
                _recipeInfo.value = it
            }.onFailure {
                Timber.e(it, "loadRecipeInfo: can't load recipe info")
            }
        }
    }
}