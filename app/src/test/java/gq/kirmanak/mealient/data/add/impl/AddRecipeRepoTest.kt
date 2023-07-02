package gq.kirmanak.mealient.data.add.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.datastore_test.PORRIDGE_RECIPE_DRAFT
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.model_mapper.ModelMapperImpl
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeRepoTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var dataSource: AddRecipeDataSource

    @MockK(relaxUnitFun = true)
    lateinit var storage: AddRecipeStorage

    private val modelMapper: ModelMapper = ModelMapperImpl()

    private lateinit var subject: AddRecipeRepo

    @Before
    override fun setUp() {
        super.setUp()
        subject = AddRecipeRepoImpl(dataSource, storage, logger, modelMapper)
    }

    @Test
    fun `when clear expect storage clear`() = runTest {
        subject.clear()
        coVerify { storage.clear() }
    }

    @Test
    fun `when saveRecipe expect then reads storage`() = runTest {
        every { storage.updates } returns flowOf(PORRIDGE_RECIPE_DRAFT)
        coEvery { dataSource.addRecipe(any()) } returns "porridge"
        subject.saveRecipe()
        verify { storage.updates }
    }

    @Test
    fun `when saveRecipe expect addRecipe with stored value`() = runTest {
        every { storage.updates } returns flowOf(PORRIDGE_RECIPE_DRAFT)
        coEvery { dataSource.addRecipe(any()) } returns "porridge"
        subject.saveRecipe()
        coVerify { dataSource.addRecipe(eq(PORRIDGE_ADD_RECIPE_INFO)) }
    }

    @Test
    fun `when saveRecipe expect result from dataSource`() = runTest {
        every { storage.updates } returns flowOf(PORRIDGE_RECIPE_DRAFT)
        val expected = "porridge"
        coEvery { dataSource.addRecipe(any()) } returns expected
        assertThat(subject.saveRecipe()).isEqualTo(expected)
    }

    @Test
    fun `when preserve expect save to storage`() = runTest {
        subject.preserve(PORRIDGE_ADD_RECIPE_INFO)
        coVerify { storage.save(eq(PORRIDGE_RECIPE_DRAFT)) }
    }
}