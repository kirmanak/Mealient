package gq.kirmanak.mealient.ui.recipes

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.ui.recipes.list.RecipeListEvent
import gq.kirmanak.mealient.ui.recipes.list.RecipeListItemState
import gq.kirmanak.mealient.ui.recipes.list.RecipeListSnackbar
import gq.kirmanak.mealient.ui.recipes.list.RecipesListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

internal class RecipesListViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var authRepo: AuthRepo

    @MockK(relaxed = true)
    lateinit var recipeRepo: RecipeRepo

    @MockK(relaxed = true)
    lateinit var recipeImageUrlProvider: RecipeImageUrlProvider

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
    fun `when SearchQueryChanged happens with query expect call to recipe repo`() {
        val subject = createSubject()
        subject.onEvent(RecipeListEvent.SearchQueryChanged("query"))
        verify { recipeRepo.updateNameQuery("query") }
    }

    @Test
    fun `when recipe is clicked expect call to repo`() = runTest {
        coEvery { recipeRepo.refreshRecipeInfo(eq("cake")) } returns Result.success(Unit)
        val subject = createSubject()
        val recipe = RecipeListItemState(
            imageUrl = null,
            showFavoriteIcon = true,
            entity = CAKE_RECIPE_SUMMARY_ENTITY,
        )
        subject.onEvent(RecipeListEvent.RecipeClick(recipe))
        coVerify { recipeRepo.refreshRecipeInfo("cake") }
    }

    @Test
    fun `when recipe is clicked and refresh succeeds expect id to open`() = runTest {
        coEvery { recipeRepo.refreshRecipeInfo(eq("cake")) } returns Result.success(Unit)
        val subject = createSubject()
        val recipe = RecipeListItemState(
            imageUrl = null,
            showFavoriteIcon = true,
            entity = CAKE_RECIPE_SUMMARY_ENTITY,
        )
        subject.onEvent(RecipeListEvent.RecipeClick(recipe))
        assertThat(subject.screenState.value.recipeIdToOpen).isEqualTo("1")
    }

    @Test
    fun `when recipe is clicked and refresh fails expect id to open`() = runTest {
        coEvery { recipeRepo.refreshRecipeInfo(eq("cake")) } returns Result.failure(IOException())
        val subject = createSubject()
        val recipe = RecipeListItemState(
            imageUrl = null,
            showFavoriteIcon = true,
            entity = CAKE_RECIPE_SUMMARY_ENTITY,
        )
        subject.onEvent(RecipeListEvent.RecipeClick(recipe))
        assertThat(subject.screenState.value.recipeIdToOpen).isEqualTo("1")
    }


    @Test
    fun `when delete recipe expect successful result in flow`() = runTest {
        coEvery { recipeRepo.deleteRecipe(any()) } returns Result.failure(IOException())
        val subject = createSubject()
        val recipe = RecipeListItemState(
            imageUrl = null,
            showFavoriteIcon = true,
            entity = CAKE_RECIPE_SUMMARY_ENTITY,
        )
        subject.onEvent(RecipeListEvent.DeleteConfirmed(recipe))
        assertThat(subject.screenState.value.snackbarState).isEqualTo(RecipeListSnackbar.DeleteFailed)
    }

    private fun createSubject() = RecipesListViewModel(
        recipeRepo = recipeRepo,
        logger = logger,
        recipeImageUrlProvider = recipeImageUrlProvider,
        authRepo = authRepo,
    )
}