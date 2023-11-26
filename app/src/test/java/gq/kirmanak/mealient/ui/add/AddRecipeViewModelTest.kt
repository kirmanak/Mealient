package gq.kirmanak.mealient.ui.add

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class AddRecipeViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var addRecipeRepo: AddRecipeRepo

    lateinit var subject: AddRecipeViewModel

    @Before
    override fun setUp() {
        super.setUp()
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(EMPTY_ADD_RECIPE_INFO)
        subject = AddRecipeViewModel(addRecipeRepo, logger)
    }

    @Test
    fun `when saveRecipe fails then addRecipeResult is false`() = runTest {
        coEvery { addRecipeRepo.saveRecipe() } throws IllegalStateException()
        subject.onEvent(AddRecipeScreenEvent.SaveRecipeClick)
        assertThat(subject.screenState.value.snackbarMessage)
            .isEqualTo(AddRecipeSnackbarMessage.Error)
    }

    @Test
    fun `when saveRecipe succeeds then addRecipeResult is true`() = runTest {
        coEvery { addRecipeRepo.saveRecipe() } returns "recipe-slug"
        subject.onEvent(AddRecipeScreenEvent.SaveRecipeClick)
        assertThat(subject.screenState.value.snackbarMessage)
            .isEqualTo(AddRecipeSnackbarMessage.Success)
    }

    @Test
    fun `when UI is updated then preserves`() {
        subject.onEvent(AddRecipeScreenEvent.RecipeNameInput("Porridge"))
        val infoSlot = slot<AddRecipeInfo>()
        coVerify { addRecipeRepo.preserve(capture(infoSlot)) }
        assertThat(infoSlot.captured.name).isEqualTo("Porridge")
    }

    @Test
    fun `when loadPreservedRequest then updates screenState`() = runTest {
        val expected = PORRIDGE_ADD_RECIPE_INFO
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(expected)
        subject.doLoadPreservedRequest()
        val screenState = subject.screenState.value
        assertThat(screenState.recipeNameInput).isSameInstanceAs("Porridge")
        assertThat(screenState.recipeDescriptionInput).isSameInstanceAs("A tasty porridge")
        assertThat(screenState.recipeYieldInput).isSameInstanceAs("3 servings")
        assertThat(screenState.isPublicRecipe).isSameInstanceAs(true)
        assertThat(screenState.disableComments).isSameInstanceAs(false)
        assertThat(screenState.ingredients).isEqualTo(
            listOf("2 oz of white milk", "2 oz of white sugar")
        )
        assertThat(screenState.instructions).isEqualTo(
            listOf("Mix the ingredients", "Boil the ingredients")
        )
    }

    @Test
    fun `when initialized then name is empty`() {
        assertThat(subject.screenState.value.recipeNameInput).isEmpty()
    }

    @Test
    fun `when recipe name entered then screen state is updated`() {
        subject.onEvent(AddRecipeScreenEvent.RecipeNameInput("Porridge"))
        assertThat(subject.screenState.value.recipeNameInput).isEqualTo("Porridge")
    }

    @Test
    fun `when clear then updates screen state`() = runTest {
        subject.onEvent(AddRecipeScreenEvent.RecipeNameInput("Porridge"))
        subject.onEvent(AddRecipeScreenEvent.ClearInputClick)
        assertThat(subject.screenState.value.recipeNameInput).isEmpty()
    }

    companion object {
        private val EMPTY_ADD_RECIPE_INFO = AddRecipeInfo(
            name = "",
            description = "",
            recipeYield = "",
            recipeInstructions = emptyList(),
            recipeIngredient = emptyList(),
            settings = AddRecipeSettingsInfo(public = false, disableComments = false)
        )
    }
}