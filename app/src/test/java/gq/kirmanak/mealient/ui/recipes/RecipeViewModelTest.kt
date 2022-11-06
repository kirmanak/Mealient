package gq.kirmanak.mealient.ui.recipes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.FakeLogger
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    @MockK
    lateinit var authRepo: AuthRepo

    @MockK(relaxed = true)
    lateinit var recipeRepo: RecipeRepo

    private val logger: Logger = FakeLogger()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when authRepo isAuthorized changes to true expect isAuthorized update`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(false, true)
        assertThat(createSubject().isAuthorized.value).isTrue()
    }

    @Test
    fun `when authRepo isAuthorized changes to false expect isAuthorized update`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(true, false)
        assertThat(createSubject().isAuthorized.value).isFalse()
    }

    @Test
    fun `when authRepo isAuthorized doesn't change expect isAuthorized null`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(true)
        assertThat(createSubject().isAuthorized.value).isNull()
    }

    @Test
    fun `when isAuthorization change is handled expect isAuthorized null`() {
        every { authRepo.isAuthorizedFlow } returns flowOf(true, false)
        val subject = createSubject()
        subject.onAuthorizationChangeHandled()
        assertThat(subject.isAuthorized.value).isNull()
    }

    @Test
    fun `when refreshRecipeInfo succeeds expect successful result`() = runTest {
        every { authRepo.isAuthorizedFlow } returns flowOf(true)
        val slug = "cake"
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns Result.success(Unit)
        val actual = createSubject().refreshRecipeInfo(slug).asFlow().first()
        assertThat(actual).isEqualTo(Result.success(Unit))
    }

    @Test
    fun `when refreshRecipeInfo succeeds expect call to repo`() = runTest {
        every { authRepo.isAuthorizedFlow } returns flowOf(true)
        val slug = "cake"
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns Result.success(Unit)
        createSubject().refreshRecipeInfo(slug).asFlow().first()
        coVerify { recipeRepo.refreshRecipeInfo(slug) }
    }

    @Test
    fun `when refreshRecipeInfo fails expect result with error`() = runTest {
        every { authRepo.isAuthorizedFlow } returns flowOf(true)
        val slug = "cake"
        val result = Result.failure<Unit>(RuntimeException())
        coEvery { recipeRepo.refreshRecipeInfo(eq(slug)) } returns result
        val actual = createSubject().refreshRecipeInfo(slug).asFlow().first()
        assertThat(actual).isEqualTo(result)
    }

    private fun createSubject() = RecipeViewModel(recipeRepo, authRepo, logger)
}