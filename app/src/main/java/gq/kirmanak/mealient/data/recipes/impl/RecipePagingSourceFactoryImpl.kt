package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.InvalidatingPagingSourceFactory
import gq.kirmanak.mealient.database.recipe.RecipeStorage
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

    private val delegate = InvalidatingPagingSourceFactory {
        val currentQuery = query.get()
        logger.d { "Creating paging source, query is $currentQuery" }
        recipeStorage.queryRecipes(currentQuery)
    }

    override fun invoke() = delegate.invoke()

    override fun setQuery(newQuery: String?) {
        logger.v { "setQuery() called with: newQuery = $newQuery" }
        query.set(newQuery)
        invalidate()
    }

    override fun invalidate() = delegate.invalidate()
}