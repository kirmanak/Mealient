package gq.kirmanak.mealie.data.recipes.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealie.data.recipes.RecipeRepo
import gq.kirmanak.mealie.data.recipes.db.RecipeStorage
import gq.kirmanak.mealie.data.recipes.db.RecipeSummaryEntity
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
class RecipeRepoImpl @Inject constructor(
    private val mediator: RecipesRemoteMediator,
    private val storage: RecipeStorage,
    private val pagingSourceFactory: RecipePagingSourceFactory
) : RecipeRepo {
    override fun createPager(): Pager<Int, RecipeSummaryEntity> {
        Timber.v("createPager() called")
        val pagingConfig = PagingConfig(pageSize = 30, enablePlaceholders = true)
        return Pager(
            config = pagingConfig,
            remoteMediator = mediator,
            pagingSourceFactory = pagingSourceFactory
        )
    }

    override suspend fun clearLocalData() {
        Timber.v("clearLocalData() called")
        storage.clearAllLocalData()
    }
}