package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.PagingSource
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import timber.log.Timber
import java.util.concurrent.ConcurrentSkipListSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipePagingSourceFactory @Inject constructor(
    private val recipeStorage: RecipeStorage
) : () -> PagingSource<Int, RecipeSummaryEntity> {
    private val sources: MutableSet<PagingSource<Int, RecipeSummaryEntity>> =
        ConcurrentSkipListSet(PagingSourceComparator)

    override fun invoke(): PagingSource<Int, RecipeSummaryEntity> {
        Timber.v("invoke() called")
        val newSource = recipeStorage.queryRecipes()
        sources.add(newSource)
        return newSource
    }

    fun invalidate() {
        Timber.v("invalidate() called")
        for (source in sources) {
            if (!source.invalid) {
                source.invalidate()
            }
        }
        sources.removeAll { it.invalid }
    }

    private object PagingSourceComparator : Comparator<PagingSource<Int, RecipeSummaryEntity>> {
        override fun compare(
            left: PagingSource<Int, RecipeSummaryEntity>?,
            right: PagingSource<Int, RecipeSummaryEntity>?
        ): Int {
            val leftHash = left?.hashCode() ?: 0
            val rightHash = right?.hashCode() ?: 0
            return leftHash - rightHash
        }
    }
}