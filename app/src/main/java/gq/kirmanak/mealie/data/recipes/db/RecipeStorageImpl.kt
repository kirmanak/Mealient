package gq.kirmanak.mealie.data.recipes.db

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealie.data.MealieDb
import gq.kirmanak.mealie.data.recipes.network.GetRecipeSummaryResponse
import timber.log.Timber
import javax.inject.Inject

class RecipeStorageImpl @Inject constructor(
    private val db: MealieDb
) : RecipeStorage {
    private val recipeDao: RecipeDao by lazy { db.recipeDao() }

    override suspend fun saveRecipes(
        recipes: List<GetRecipeSummaryResponse>
    ) = db.withTransaction {
        Timber.v("saveRecipes() called with $recipes")

        val tagEntities = mutableSetOf<TagEntity>()
        tagEntities.addAll(recipeDao.queryAllTags())

        val categoryEntities = mutableSetOf<CategoryEntity>()
        categoryEntities.addAll(recipeDao.queryAllCategories())

        val tagRecipeEntities = mutableSetOf<TagRecipeEntity>()
        val categoryRecipeEntities = mutableSetOf<CategoryRecipeEntity>()

        for (recipe in recipes) {
            val recipeId = recipeDao.insertRecipe(recipe.recipeEntity())

            for (tag in recipe.tags) {
                val tagId = getIdOrInsert(tagEntities, tag)
                tagRecipeEntities += TagRecipeEntity(tagId, recipeId)
            }

            for (category in recipe.recipeCategories) {
                val categoryId = getOrInsert(categoryEntities, category)
                categoryRecipeEntities += CategoryRecipeEntity(categoryId, recipeId)
            }
        }

        recipeDao.insertTagRecipeEntities(tagRecipeEntities)
        recipeDao.insertCategoryRecipeEntities(categoryRecipeEntities)
    }

    private suspend fun getOrInsert(
        categoryEntities: MutableSet<CategoryEntity>,
        category: String
    ): Long {
        val existingCategory = categoryEntities.find { it.name == category }
        val categoryId = if (existingCategory == null) {
            val categoryEntity = CategoryEntity(name = category)
            val newId = recipeDao.insertCategory(categoryEntity)
            categoryEntities.add(categoryEntity.copy(localId = newId))
            newId
        } else {
            existingCategory.localId
        }
        return categoryId
    }

    private suspend fun getIdOrInsert(
        tagEntities: MutableSet<TagEntity>,
        tag: String
    ): Long {
        val existingTag = tagEntities.find { it.name == tag }
        val tagId = if (existingTag == null) {
            val tagEntity = TagEntity(name = tag)
            val newId = recipeDao.insertTag(tagEntity)
            tagEntities.add(tagEntity.copy(localId = newId))
            newId
        } else {
            existingTag.localId
        }
        return tagId
    }

    private fun GetRecipeSummaryResponse.recipeEntity() = RecipeEntity(
        remoteId = remoteId,
        name = name,
        slug = slug,
        image = image,
        description = description,
        rating = rating,
        dateAdded = dateAdded,
        dateUpdated = dateUpdated,
    )

    override fun queryRecipes(): PagingSource<Int, RecipeEntity> {
        Timber.v("queryRecipes() called")
        return recipeDao.queryRecipesByPages()
    }

    override suspend fun refreshAll(recipes: List<GetRecipeSummaryResponse>) {
        Timber.v("refreshAll() called with: recipes = $recipes")
        db.withTransaction {
            recipeDao.removeAllRecipes()
            saveRecipes(recipes)
        }
    }

    override suspend fun clearAllLocalData() {
        Timber.v("clearAllLocalData() called")
        db.withTransaction {
            recipeDao.removeAllRecipes()
            recipeDao.removeAllCategories()
            recipeDao.removeAllTags()
        }
    }
}