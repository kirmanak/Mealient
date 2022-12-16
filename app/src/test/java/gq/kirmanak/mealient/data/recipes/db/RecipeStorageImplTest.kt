package gq.kirmanak.mealient.data.recipes.db

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.test.HiltRobolectricTest
import gq.kirmanak.mealient.test.RecipeImplTestData.BREAD_INGREDIENT
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_BREAD_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.FULL_CAKE_INFO_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.FULL_PORRIDGE_INFO_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.MIX_INSTRUCTION
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.test.RecipeImplTestData.PORRIDGE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.TEST_RECIPE_SUMMARY_ENTITIES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class RecipeStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipeStorageImpl

    @Inject
    lateinit var appDb: AppDb

    @Test
    fun `when saveRecipes then saves recipes`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        val actualTags = appDb.recipeDao().queryAllRecipes()
        assertThat(actualTags).containsExactly(
            CAKE_RECIPE_SUMMARY_ENTITY,
            PORRIDGE_RECIPE_SUMMARY_ENTITY
        )
    }

    @Test
    fun `when refreshAll then old recipes aren't preserved`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.refreshAll(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        val actual = appDb.recipeDao().queryAllRecipes()
        assertThat(actual).containsExactly(CAKE_RECIPE_SUMMARY_ENTITY)
    }

    @Test
    fun `when clearAllLocalData then recipes aren't preserved`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.clearAllLocalData()
        val actual = appDb.recipeDao().queryAllRecipes()
        assertThat(actual).isEmpty()
    }

    @Test
    fun `when saveRecipeInfo then saves recipe info`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(CAKE_FULL_RECIPE_INFO)
        val actual = appDb.recipeDao().queryFullRecipeInfo("1")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo with two then saves second`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.saveRecipeInfo(CAKE_FULL_RECIPE_INFO)
        subject.saveRecipeInfo(PORRIDGE_FULL_RECIPE_INFO)
        val actual = appDb.recipeDao().queryFullRecipeInfo("2")
        assertThat(actual).isEqualTo(FULL_PORRIDGE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo secondly then overwrites ingredients`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(CAKE_FULL_RECIPE_INFO)
        val newRecipe = CAKE_FULL_RECIPE_INFO.copy(recipeIngredients = listOf(BREAD_INGREDIENT))
        subject.saveRecipeInfo(newRecipe)
        val actual = appDb.recipeDao().queryFullRecipeInfo("1")?.recipeIngredients
        val expected = listOf(CAKE_BREAD_RECIPE_INGREDIENT_ENTITY.copy(localId = 3))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when saveRecipeInfo secondly then overwrites instructions`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(CAKE_FULL_RECIPE_INFO)
        val newRecipe = CAKE_FULL_RECIPE_INFO.copy(recipeInstructions = listOf(MIX_INSTRUCTION))
        subject.saveRecipeInfo(newRecipe)
        val actual = appDb.recipeDao().queryFullRecipeInfo("1")?.recipeInstructions
        val expected = listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY.copy(localId = 3))
        assertThat(actual).isEqualTo(expected)
    }
}