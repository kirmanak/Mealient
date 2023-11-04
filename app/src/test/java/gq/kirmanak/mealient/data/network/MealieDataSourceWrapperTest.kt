package gq.kirmanak.mealient.data.network

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_CREATE_RECIPE_REQUEST
import gq.kirmanak.mealient.datasource_test.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_RESPONSE
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_SUMMARY_RESPONSE
import gq.kirmanak.mealient.datasource_test.PORRIDGE_UPDATE_RECIPE_REQUEST
import gq.kirmanak.mealient.datasource_test.RECIPE_SUMMARY_PORRIDGE
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.model_mapper.ModelMapperImpl
import gq.kirmanak.mealient.test.AuthImplTestData.FAVORITE_RECIPES_LIST
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.USER_INFO
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MealieDataSourceWrapperTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var dataSource: MealieDataSource

    private val modelMapper: ModelMapper = ModelMapperImpl()

    lateinit var subject: MealieDataSourceWrapper

    @Before
    override fun setUp() {
        super.setUp()
        subject = MealieDataSourceWrapper(dataSource, modelMapper)
        coEvery { dataSource.requestUserInfo() } returns USER_INFO
    }

    @Test
    fun `when requestRecipeInfo expect a valid network call`() = runTest {
        val slug = "porridge"
        coEvery { dataSource.requestRecipeInfo(eq(slug)) } returns PORRIDGE_RECIPE_RESPONSE
        coEvery { authRepo.getAuthToken() } returns TEST_TOKEN

        val actual = subject.requestRecipeInfo(slug)

        coVerify { dataSource.requestRecipeInfo(eq(slug)) }

        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when requestRecipes expect valid network request`() = runTest {
        coEvery {
            dataSource.requestRecipes(any(), any())
        } returns listOf(PORRIDGE_RECIPE_SUMMARY_RESPONSE)
        coEvery { authRepo.getAuthToken() } returns TEST_TOKEN

        val actual = subject.requestRecipes(40, 10)

        val page = 5 // 0-9 (1), 10-19 (2), 20-29 (3), 30-39 (4), 40-49 (5)
        val perPage = 10
        coVerify {
            dataSource.requestRecipes(eq(page), eq(perPage))
        }

        assertThat(actual).isEqualTo(listOf(RECIPE_SUMMARY_PORRIDGE))
    }

    @Test(expected = IOException::class)
    fun `when request fails expect createRecipe to rethrow`() = runTest {
        coEvery { dataSource.createRecipe(any()) } throws IOException()
        coEvery { authRepo.getAuthToken() } returns TEST_TOKEN
        subject.addRecipe(PORRIDGE_ADD_RECIPE_INFO)
    }

    @Test
    fun `when create recipe expect createRecipe to call in sequence`() = runTest {
        val slug = "porridge"

        coEvery { dataSource.createRecipe(any()) } returns slug
        coEvery {
            dataSource.updateRecipe(any(), any())
        } returns PORRIDGE_RECIPE_RESPONSE
        coEvery { authRepo.getAuthToken() } returns TEST_TOKEN

        val actual = subject.addRecipe(PORRIDGE_ADD_RECIPE_INFO)

        coVerifySequence {
            dataSource.createRecipe(
                eq(PORRIDGE_CREATE_RECIPE_REQUEST),
            )

            dataSource.updateRecipe(
                eq(slug),
                eq(PORRIDGE_UPDATE_RECIPE_REQUEST),
            )
        }

        assertThat(actual).isEqualTo(slug)
    }

    @Test
    fun `when remove favorite recipe info expect correct sequence`() = runTest {
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = false)
        coVerify {
            dataSource.requestUserInfo()
            dataSource.removeFavoriteRecipe(eq("userId"), eq("cake"))
        }
    }

    @Test
    fun `when add favorite recipe info expect correct sequence`() = runTest {
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = true)
        coVerify {
            dataSource.requestUserInfo()
            dataSource.addFavoriteRecipe(eq("userId"), eq("cake"))
        }
    }

    @Test
    fun `when get favorite recipes expect correct call`() = runTest {
        subject.getFavoriteRecipes()
        coVerify { dataSource.requestUserInfo() }
    }

    @Test
    fun `when get favorite recipes expect correct result`() = runTest {
        assertThat(subject.getFavoriteRecipes()).isEqualTo(FAVORITE_RECIPES_LIST)
    }
}