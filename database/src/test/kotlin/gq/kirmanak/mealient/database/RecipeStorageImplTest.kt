package gq.kirmanak.mealient.database

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.RecipeStorageImpl
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
internal class RecipeStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipeStorageImpl

    @Inject
    lateinit var recipeDao: RecipeDao

    @Test
    fun `when saveRecipes then saves recipes`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        val actualTags = recipeDao.queryAllRecipes()
        assertThat(actualTags).containsExactly(
            CAKE_RECIPE_SUMMARY_ENTITY,
            PORRIDGE_RECIPE_SUMMARY_ENTITY
        )
    }

    @Test
    fun `when refreshAll then old recipes aren't preserved`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.refreshAll(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        val actual = recipeDao.queryAllRecipes()
        assertThat(actual).containsExactly(CAKE_RECIPE_SUMMARY_ENTITY)
    }

    @Test
    fun `when clearAllLocalData then recipes aren't preserved`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.clearAllLocalData()
        val actual = recipeDao.queryAllRecipes()
        assertThat(actual).isEmpty()
    }

    @Test
    fun `when saveRecipeInfo then saves recipe info`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY, CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY),
            listOf(
                MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY,
                MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY
            ),
        )
        val actual = recipeDao.queryFullRecipeInfo("1")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo with two then saves second`() = runTest {
        subject.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY, CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY),
            listOf(
                MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY,
                MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY
            ),
        )
        subject.saveRecipeInfo(
            PORRIDGE_RECIPE_ENTITY_FULL,
            listOf(PORRIDGE_SUGAR_RECIPE_INGREDIENT_ENTITY, PORRIDGE_MILK_RECIPE_INGREDIENT_ENTITY),
            listOf(PORRIDGE_MIX_RECIPE_INSTRUCTION_ENTITY, PORRIDGE_BOIL_RECIPE_INSTRUCTION_ENTITY),
            emptyList(),
        )
        val actual = recipeDao.queryFullRecipeInfo("2")
        assertThat(actual).isEqualTo(FULL_PORRIDGE_INFO_ENTITY)
    }

    @Test
    fun `when saveRecipeInfo twice then overwrites ingredients`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY, CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY),
            listOf(
                MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY,
                MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY
            ),
        )
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY),
            emptyList(),
        )
        val actual = recipeDao.queryFullRecipeInfo("1")?.recipeIngredients
        val expected = listOf(CAKE_BREAD_RECIPE_INGREDIENT_ENTITY)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when saveRecipeInfo twice then overwrites instructions`() = runTest {
        subject.saveRecipes(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY, CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY),
            listOf(
                MIX_SUGAR_RECIPE_INGREDIENT_INSTRUCTION_ENTITY,
                MIX_BREAD_RECIPE_INGREDIENT_INSTRUCTION_ENTITY
            ),
        )
        subject.saveRecipeInfo(
            CAKE_RECIPE_ENTITY,
            listOf(CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY, CAKE_BREAD_RECIPE_INGREDIENT_ENTITY),
            listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY),
            emptyList(),
        )
        val actual = recipeDao.queryFullRecipeInfo("1")?.recipeInstructions
        val expected = listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY)
        assertThat(actual).isEqualTo(expected)
    }
}