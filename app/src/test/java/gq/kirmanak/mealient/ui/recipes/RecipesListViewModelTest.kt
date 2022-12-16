package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.asFlow
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.test.RecipeImplTestData.CAKE_RECIPE_SUMMARY_ENTITY
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesListViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var authRepo: AuthRepo

    @MockK(relaxed = true)
    lateinit var recipeRepo: RecipeRepo

    @Before
    override fun setUp() {
        super.setUp()
        every { authRepo.isAuthorizedFlow } returns flowOf(true)
    }

    @Test
    fun `when authRepo isAuthorized changes to true expect that recipes are refreshed`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(false, true)
        createSubject()
        coVerify { recipeRepo.refreshRecipes() }
    }

    @Test
    fun `when authRepo isAuthorized changes to false expect that recipes are not refreshed`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(true, false)
        createSubject()
        coVerify(inverse = true) { recipeRepo.refreshRecipes() }
    }

    @Test
    fun `when authRepo isAuthorized doesn't change expect that recipes are not refreshed`() {
        createSubject()
        coVerify(inverse = true) { recipeRepo.refreshRecipes() }
    }

    @Test
    fun `when refreshRecipeInfo succeeds expect successful result`() = runTest {
        val slug = "cake"
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns Result.success(Unit)
        val actual = createSubject().refreshRecipeInfo(slug).asFlow().first()
        assertThat(actual).isEqualTo(Result.success(Unit))
    }

    @Test
    fun `when refreshRecipeInfo succeeds expect call to repo`() = runTest {
        val slug = "cake"
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns Result.success(Unit)
        createSubject().refreshRecipeInfo(slug).asFlow().first()
        coVerify { recipeRepo.refreshRecipeInfo(slug) }
    }

    @Test
    fun `when refreshRecipeInfo fails expect result with error`() = runTest {
        val slug = "cake"
        val result = Result.failure<Unit>(RuntimeException())
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns result
        val actual = createSubject().refreshRecipeInfo(slug).asFlow().first()
        assertThat(actual).isEqualTo(result)
    }

    @Test
    fun `when delete recipe expect successful result in flow`() = runTest {
        coEvery { recipeRepo.deleteRecipe(any()) } returns Result.success(Unit)
        val subject = createSubject()
        val results = runTestAndCollectFlow(subject.deleteRecipeResult) {
            subject.onDeleteConfirm(CAKE_RECIPE_SUMMARY_ENTITY)
        }
        assertThat(results.single().isSuccess).isTrue()
    }

    @Test
    fun `when delete recipe expect failed result in flow`() = runTest {
        coEvery { recipeRepo.deleteRecipe(any()) } returns Result.failure(IOException())
        val subject = createSubject()
        val results = runTestAndCollectFlow(subject.deleteRecipeResult) {
            subject.onDeleteConfirm(CAKE_RECIPE_SUMMARY_ENTITY)
        }
        assertThat(results.single().isFailure).isTrue()
    }

    private inline fun <T> TestScope.runTestAndCollectFlow(
        flow: Flow<T>,
        block: () -> Unit,
    ): List<T> {
        val results = mutableListOf<T>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.toList(results)
        }
        block()
        collectJob.cancel()
        return results
    }

    private fun createSubject() = RecipesListViewModel(recipeRepo, authRepo, logger)
}