package gq.kirmanak.mealient.data.recipes.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.AppDb
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.test.MockServerWithAuthTest
import gq.kirmanak.mealient.data.test.RecipeImplTestData.FULL_CAKE_INFO_ENTITY
import gq.kirmanak.mealient.data.test.RecipeImplTestData.RECIPE_SUMMARY_CAKE
import gq.kirmanak.mealient.data.test.RecipeImplTestData.enqueueSuccessfulGetRecipe
import gq.kirmanak.mealient.data.test.RecipeImplTestData.enqueueUnsuccessfulRecipeResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RecipeRepoImplTest : MockServerWithAuthTest() {
    @Inject
    lateinit var subject: RecipeRepo

    @Inject
    lateinit var storage: RecipeStorage

    @Inject
    lateinit var appDb: AppDb

    @Test
    fun `when loadRecipeInfo then loads recipe`(): Unit = runBlocking {
        storage.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        mockServer.enqueueSuccessfulGetRecipe()
        val actual = subject.loadRecipeInfo(1, "cake")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when loadRecipeInfo then saves to DB`(): Unit = runBlocking {
        storage.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        mockServer.enqueueSuccessfulGetRecipe()
        subject.loadRecipeInfo(1, "cake")
        val actual = appDb.recipeDao().queryFullRecipeInfo(1)
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when loadRecipeInfo with error then loads from DB`(): Unit = runBlocking {
        storage.saveRecipes(listOf(RECIPE_SUMMARY_CAKE))
        mockServer.enqueueSuccessfulGetRecipe()
        subject.loadRecipeInfo(1, "cake")
        mockServer.enqueueUnsuccessfulRecipeResponse()
        val actual = subject.loadRecipeInfo(1, "cake")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }
}