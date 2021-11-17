package gq.kirmanak.mealie.data.recipes.db

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.MealieDb
import gq.kirmanak.mealie.data.auth.impl.HiltRobolectricTest
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.PORRIDGE_RECIPE_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.RECIPE_SUMMARY_CAKE
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.TEST_RECIPE_SUMMARIES
import gq.kirmanak.mealie.data.recipes.db.entity.CategoryEntity
import gq.kirmanak.mealie.data.recipes.db.entity.CategoryRecipeEntity
import gq.kirmanak.mealie.data.recipes.db.entity.TagEntity
import gq.kirmanak.mealie.data.recipes.db.entity.TagRecipeEntity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RecipeStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipeStorageImpl

    @Inject
    lateinit var mealieDb: MealieDb

    @Test
    fun `when saveRecipes then saves tags`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actualTags = mealieDb.recipeDao().queryAllTags()
        assertThat(actualTags).containsExactly(
            TagEntity(localId = 1, name = "gluten"),
            TagEntity(localId = 2, name = "allergic"),
            TagEntity(localId = 3, name = "milk")
        )
    }

    @Test
    fun `when saveRecipes then saves categories`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllCategories()
        assertThat(actual).containsExactly(
            CategoryEntity(localId = 1, name = "dessert"),
            CategoryEntity(localId = 2, name = "tasty"),
            CategoryEntity(localId = 3, name = "porridge")
        )
    }

    @Test
    fun `when saveRecipes then saves recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actualTags = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actualTags).containsExactly(CAKE_RECIPE_ENTITY, PORRIDGE_RECIPE_ENTITY)
    }

    @Test
    fun `when saveRecipes then saves category recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllCategoryRecipes()
        assertThat(actual).containsExactly(
            CategoryRecipeEntity(categoryId = 1, recipeId = 1),
            CategoryRecipeEntity(categoryId = 2, recipeId = 1),
            CategoryRecipeEntity(categoryId = 3, recipeId = 2),
            CategoryRecipeEntity(categoryId = 2, recipeId = 2)
        )
    }

    @Test
    fun `when saveRecipes then saves tag recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllTagRecipes()
        assertThat(actual).containsExactly(
            TagRecipeEntity(tagId = 1, recipeId = 1),
            TagRecipeEntity(tagId = 2, recipeId = 1),
            TagRecipeEntity(tagId = 3, recipeId = 2),
            TagRecipeEntity(tagId = 1, recipeId = 2),
        )
    }

    @Test
    fun `when refreshAll then old recipes aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.refreshAll(listOf(RECIPE_SUMMARY_CAKE))
        val actual = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actual).containsExactly(CAKE_RECIPE_ENTITY)
    }

    @Test
    fun `when refreshAll then old category recipes aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.refreshAll(listOf(RECIPE_SUMMARY_CAKE))
        val actual = mealieDb.recipeDao().queryAllCategoryRecipes()
        assertThat(actual).containsExactly(
            CategoryRecipeEntity(categoryId = 1, recipeId = 1),
            CategoryRecipeEntity(categoryId = 2, recipeId = 1),
        )
    }

    @Test
    fun `when refreshAll then old tag recipes aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.refreshAll(listOf(RECIPE_SUMMARY_CAKE))
        val actual = mealieDb.recipeDao().queryAllTagRecipes()
        assertThat(actual).containsExactly(
            TagRecipeEntity(tagId = 1, recipeId = 1),
            TagRecipeEntity(tagId = 2, recipeId = 1),
        )
    }

    @Test
    fun `when clearAllLocalData then recipes aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.clearAllLocalData()
        val actual = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actual).isEmpty()
    }

    @Test
    fun `when clearAllLocalData then categories aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.clearAllLocalData()
        val actual = mealieDb.recipeDao().queryAllCategories()
        assertThat(actual).isEmpty()
    }

    @Test
    fun `when clearAllLocalData then tags aren't preserved`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        subject.clearAllLocalData()
        val actual = mealieDb.recipeDao().queryAllTags()
        assertThat(actual).isEmpty()
    }
}