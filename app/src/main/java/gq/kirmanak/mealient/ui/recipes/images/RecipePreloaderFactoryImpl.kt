package gq.kirmanak.mealient.ui.recipes.images

import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import javax.inject.Inject

@FragmentScoped
class RecipePreloaderFactoryImpl @Inject constructor(
    private val recipePreloadModelProvider: RecipePreloadModelProvider.Factory,
    private val fragment: Fragment,
) : RecipePreloaderFactory {

    override fun create(
        adapter: PagingDataAdapter<RecipeSummaryEntity, *>,
    ): RecyclerViewPreloader<RecipeSummaryEntity> {
        val preloadSizeProvider = ViewPreloadSizeProvider<RecipeSummaryEntity>()
        val preloadModelProvider = recipePreloadModelProvider.create(adapter)
        return RecyclerViewPreloader(
            fragment,
            preloadModelProvider,
            preloadSizeProvider,
            MAX_PRELOAD
        )
    }

    companion object {
        const val MAX_PRELOAD = 10
    }
}