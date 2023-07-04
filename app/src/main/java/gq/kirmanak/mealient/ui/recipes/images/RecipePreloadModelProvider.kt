package gq.kirmanak.mealient.ui.recipes.images

import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger

class RecipePreloadModelProvider @AssistedInject constructor(
    @Assisted private val adapter: PagingDataAdapter<RecipeSummaryEntity, *>,
    private val fragment: Fragment,
    private val requestOptions: RequestOptions,
    private val logger: Logger,
) : ListPreloader.PreloadModelProvider<RecipeSummaryEntity> {

    override fun getPreloadItems(position: Int): List<RecipeSummaryEntity> {
        logger.v { "getPreloadItems() called with: position = $position" }
        return adapter.peek(position)?.let { listOf(it) } ?: emptyList()
    }

    override fun getPreloadRequestBuilder(item: RecipeSummaryEntity): RequestBuilder<*> {
        logger.v { "getPreloadRequestBuilder() called with: item = $item" }
        return Glide.with(fragment).load(item).apply(requestOptions)
    }

    @AssistedFactory
    interface Factory {

        fun create(adapter: PagingDataAdapter<RecipeSummaryEntity, *>): RecipePreloadModelProvider
    }
}