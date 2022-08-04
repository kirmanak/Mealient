package gq.kirmanak.mealient.ui.recipes.images

import androidx.paging.PagingDataAdapter
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipePreloaderFactory {

    fun create(adapter: PagingDataAdapter<RecipeSummaryEntity, *>): RecyclerViewPreloader<RecipeSummaryEntity>
}