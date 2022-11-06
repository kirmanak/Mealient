package gq.kirmanak.mealient.ui.recipes.info

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.FakeLogger
import gq.kirmanak.mealient.test.RecipeImplTestData.FULL_CAKE_INFO_ENTITY
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeInfoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val logger: Logger = FakeLogger()

    @MockK
    lateinit var recipeRepo: RecipeRepo

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
    fun `when recipe isn't found then UI state is empty`() = runTest {
        coEvery { recipeRepo.loadRecipeInfo(eq(RECIPE_ID)) } returns null
        val uiState = createSubject().uiState.asFlow().first()
        assertThat(uiState).isEqualTo(RecipeInfoUiState())
    }

    @Test
    fun `when recipe is found then UI state has data`() = runTest {
        val emptyNoteIngredient = RecipeIngredientEntity(recipeId = "42", note = "")
        val returnedEntity = FULL_CAKE_INFO_ENTITY.copy(
            recipeIngredients = FULL_CAKE_INFO_ENTITY.recipeIngredients + emptyNoteIngredient
        )
        coEvery { recipeRepo.loadRecipeInfo(eq(RECIPE_ID)) } returns returnedEntity
        val expected = RecipeInfoUiState(
            showIngredients = true,
            showInstructions = true,
            summaryEntity = FULL_CAKE_INFO_ENTITY.recipeSummaryEntity,
            recipeIngredients = FULL_CAKE_INFO_ENTITY.recipeIngredients,
            recipeInstructions = FULL_CAKE_INFO_ENTITY.recipeInstructions,
            title = FULL_CAKE_INFO_ENTITY.recipeSummaryEntity.name,
            description = FULL_CAKE_INFO_ENTITY.recipeSummaryEntity.description,
        )
        val actual = createSubject().uiState.asFlow().first()
        assertThat(actual).isEqualTo(expected)
    }

    private fun createSubject(): RecipeInfoViewModel {
        val argument = RecipeInfoFragmentArgs(RECIPE_ID).toSavedStateHandle()
        return RecipeInfoViewModel(recipeRepo, logger, argument)
    }

    companion object {
        private const val RECIPE_ID = "1"
    }
}