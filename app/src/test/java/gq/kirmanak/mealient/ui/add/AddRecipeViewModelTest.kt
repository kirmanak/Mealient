package gq.kirmanak.mealient.ui.add

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var addRecipeRepo: AddRecipeRepo

    lateinit var subject: AddRecipeViewModel

    @Before
    override fun setUp() {
        super.setUp()
        subject = AddRecipeViewModel(addRecipeRepo, logger)
    }

    @Test
    fun `when saveRecipe fails then addRecipeResult is false`() = runTest {
        coEvery { addRecipeRepo.saveRecipe() } throws IllegalStateException()
        subject.saveRecipe()
        assertThat(subject.addRecipeResult.first()).isFalse()
    }

    @Test
    fun `when saveRecipe succeeds then addRecipeResult is true`() = runTest {
        coEvery { addRecipeRepo.saveRecipe() } returns "recipe-slug"
        subject.saveRecipe()
        assertThat(subject.addRecipeResult.first()).isTrue()
    }

    @Test
    fun `when preserve then doesn't update UI`() {
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(PORRIDGE_ADD_RECIPE_INFO)
        subject.preserve(PORRIDGE_ADD_RECIPE_INFO)
        coVerify(inverse = true) { addRecipeRepo.addRecipeRequestFlow }
    }

    @Test
    fun `when preservedAddRecipeRequest without loadPreservedRequest then empty`() = runTest {
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(PORRIDGE_ADD_RECIPE_INFO)
        val actual = withTimeoutOrNull(10) { subject.preservedAddRecipeRequest.firstOrNull() }
        assertThat(actual).isNull()
    }

    @Test
    fun `when loadPreservedRequest then updates preservedAddRecipeRequest`() = runTest {
        val expected = PORRIDGE_ADD_RECIPE_INFO
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(expected)
        subject.loadPreservedRequest()
        assertThat(subject.preservedAddRecipeRequest.first()).isSameInstanceAs(expected)
    }

    @Test
    fun `when clear then updates preservedAddRecipeRequest`() = runTest {
        val expected = PORRIDGE_ADD_RECIPE_INFO
        coEvery { addRecipeRepo.addRecipeRequestFlow } returns flowOf(expected)
        subject.clear()
        assertThat(subject.preservedAddRecipeRequest.first()).isSameInstanceAs(expected)
    }
}