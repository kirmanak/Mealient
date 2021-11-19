package gq.kirmanak.mealie.data.recipes.db

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.MealieDb
import gq.kirmanak.mealie.data.auth.impl.HiltRobolectricTest
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.BREAD_INGREDIENT
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.CAKE_BREAD_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.FULL_CAKE_INFO_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.FULL_PORRIDGE_INFO_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.GET_CAKE_RESPONSE
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.GET_PORRIDGE_RESPONSE
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.MIX_INSTRUCTION
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.PORRIDGE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.RECIPE_SUMMARY_CAKE
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.RECIPE_SUMMARY_PORRIDGE
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
        assertThat(actualTags).containsExactly(
            CAKE_RECIPE_SUMMARY_ENTITY,
            PORRIDGE_RECIPE_SUMMARY_ENTITY
        )
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
        assertThat(actual).containsExactly(CAKE_RECIPE_SUMMARY_ENTITY)
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

    @Test
    fun `when saveRecipeInfo then saves recipe info`(): Unit = runBlocking {
        subject.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        subject.saveRecipeInfo(GET_CAKE_RESPONSE)
        val actual = mealieDb.recipeDao().queryFullRecipeInfo(1)
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo with two then saves second`(): Unit = runBlocking {
        subject.saveRecipes(listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE))
        subject.saveRecipeInfo(GET_CAKE_RESPONSE)
        subject.saveRecipeInfo(GET_PORRIDGE_RESPONSE)
        val actual = mealieDb.recipeDao().queryFullRecipeInfo(2)
        assertThat(actual).isEqualTo(FULL_PORRIDGE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo secondly then overwrites ingredients`(): Unit = runBlocking {
        subject.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        subject.saveRecipeInfo(GET_CAKE_RESPONSE)
        val newRecipe = GET_CAKE_RESPONSE.copy(recipeIngredients = listOf(BREAD_INGREDIENT))
        subject.saveRecipeInfo(newRecipe)
        val actual = mealieDb.recipeDao().queryFullRecipeInfo(1)?.recipeIngredients
        val expected = listOf(CAKE_BREAD_RECIPE_INGREDIENT_ENTITY.copy(localId = 3))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when saveRecipeInfo secondly then overwrites instructions`(): Unit = runBlocking {
        subject.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        subject.saveRecipeInfo(GET_CAKE_RESPONSE)
        val newRecipe = GET_CAKE_RESPONSE.copy(recipeInstructions = listOf(MIX_INSTRUCTION))
        subject.saveRecipeInfo(newRecipe)
        val actual = mealieDb.recipeDao().queryFullRecipeInfo(1)?.recipeInstructions
        val expected = listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY.copy(localId = 3))
        assertThat(actual).isEqualTo(expected)
    }
}