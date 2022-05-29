package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealient.data.AppDb
import gq.kirmanak.mealient.data.recipes.db.entity.*
import gq.kirmanak.mealient.data.recipes.impl.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
import gq.kirmanak.mealient.extensions.recipeEntity
import gq.kirmanak.mealient.extensions.toRecipeEntity
import gq.kirmanak.mealient.extensions.toRecipeIngredientEntity
import gq.kirmanak.mealient.extensions.toRecipeInstructionEntity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeStorageImpl @Inject constructor(
    private val db: AppDb
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
            val recipeSummaryEntity = recipe.recipeEntity()
            recipeDao.insertRecipe(recipeSummaryEntity)

            for (tag in recipe.tags) {
                val tagId = getIdOrInsert(tagEntities, tag)
                tagRecipeEntities += TagRecipeEntity(tagId, recipeSummaryEntity.remoteId)
            }

            for (category in recipe.recipeCategories) {
                val categoryId = getOrInsert(categoryEntities, category)
                categoryRecipeEntities += CategoryRecipeEntity(
                    categoryId,
                    recipeSummaryEntity.remoteId
                )
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


    override fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity> {
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

    override suspend fun saveRecipeInfo(recipe: GetRecipeResponse) {
        Timber.v("saveRecipeInfo() called with: recipe = $recipe")
        db.withTransaction {
            recipeDao.insertRecipe(recipe.toRecipeEntity())

            recipeDao.deleteRecipeIngredients(recipe.remoteId)
            val ingredients = recipe.recipeIngredients.map {
                it.toRecipeIngredientEntity(recipe.remoteId)
            }
            recipeDao.insertRecipeIngredients(ingredients)

            recipeDao.deleteRecipeInstructions(recipe.remoteId)
            val instructions = recipe.recipeInstructions.map {
                it.toRecipeInstructionEntity(recipe.remoteId)
            }
            recipeDao.insertRecipeInstructions(instructions)
        }
    }

    override suspend fun queryRecipeInfo(recipeId: Long): FullRecipeInfo {
        Timber.v("queryRecipeInfo() called with: recipeId = $recipeId")
        val fullRecipeInfo = checkNotNull(recipeDao.queryFullRecipeInfo(recipeId)) {
            "Can't find recipe by id $recipeId in DB"
        }
        Timber.v("queryRecipeInfo() returned: $fullRecipeInfo")
        return fullRecipeInfo
    }
}