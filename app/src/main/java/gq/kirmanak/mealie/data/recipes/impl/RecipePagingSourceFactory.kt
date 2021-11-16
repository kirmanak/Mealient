package gq.kirmanak.mealie.data.recipes.impl

import androidx.paging.PagingSource
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import gq.kirmanak.mealie.data.recipes.db.RecipeStorage
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipePagingSourceFactory @Inject constructor(
    private val recipeStorage: RecipeStorage
) : () -> PagingSource<Int, RecipeEntity> {
    private val sources: MutableList<PagingSource<Int, RecipeEntity>> = mutableListOf()

    override fun invoke(): PagingSource<Int, RecipeEntity> {
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
}