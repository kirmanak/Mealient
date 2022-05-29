package gq.kirmanak.mealient.ui.recipes.images

import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import timber.log.Timber
import javax.inject.Inject

class RecipePreloadModelProvider(
    private val adapter: PagingDataAdapter<RecipeSummaryEntity, *>,
    private val fragment: Fragment,
    private val requestOptions: RequestOptions,
) : ListPreloader.PreloadModelProvider<RecipeSummaryEntity> {

    override fun getPreloadItems(position: Int): List<RecipeSummaryEntity> {
        Timber.v("getPreloadItems() called with: position = $position")
        return adapter.peek(position)?.let { listOf(it) } ?: emptyList()
    }

    override fun getPreloadRequestBuilder(item: RecipeSummaryEntity): RequestBuilder<*> {
        Timber.v("getPreloadRequestBuilder() called with: item = $item")
        return Glide.with(fragment).load(item).apply(requestOptions)
    }

    @FragmentScoped
    class Factory @Inject constructor(
        private val fragment: Fragment,
        private val requestOptions: RequestOptions,
    ) {

        fun create(
            adapter: PagingDataAdapter<RecipeSummaryEntity, *>,
        ) = RecipePreloadModelProvider(adapter, fragment, requestOptions)
    }
}