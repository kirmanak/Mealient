package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.asFlow
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.database.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.ui.recipes.list.RecipesListViewModel
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

    private fun createSubject() = RecipesListViewModel(
        recipeRepo = recipeRepo,
        logger = logger,
        recipeImageUrlProvider = recipeImageUrlProvider,
        authRepo = authRepo,
    )
}