package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.PagingSource
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipePagingSourceFactoryImpl @Inject constructor(
    private val recipeStorage: RecipeStorage,
    private val logger: Logger,
) : RecipePagingSourceFactory {

    private val query = AtomicReference<String>(null)

    override fun invoke(): PagingSource<Int, RecipeSummaryEntity> {
        val currentQuery = query.get()
        logger.d { "Creating paging source, query is $currentQuery" }
        return recipeStorage.queryRecipes(currentQuery)
    }

    override fun setQuery(newQuery: String?) {
        logger.v { "setQuery() called with: newQuery = $newQuery" }
        query.set(newQuery)
    }
}