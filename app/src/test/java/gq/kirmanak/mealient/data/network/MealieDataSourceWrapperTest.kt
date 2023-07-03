package gq.kirmanak.mealient.data.network

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_ADD_RECIPE_REQUEST_V0
import gq.kirmanak.mealient.datasource_test.PORRIDGE_CREATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0
import gq.kirmanak.mealient.datasource_test.PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1
import gq.kirmanak.mealient.datasource_test.PORRIDGE_UPDATE_RECIPE_REQUEST_V1
import gq.kirmanak.mealient.datasource_test.RECIPE_SUMMARY_PORRIDGE_V0
import gq.kirmanak.mealient.datasource_test.RECIPE_SUMMARY_PORRIDGE_V1
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.model_mapper.ModelMapperImpl
import gq.kirmanak.mealient.test.AuthImplTestData.FAVORITE_RECIPES_LIST
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_SERVER_VERSION_V0
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_SERVER_VERSION_V1
import gq.kirmanak.mealient.test.AuthImplTestData.USER_INFO_V0
import gq.kirmanak.mealient.test.AuthImplTestData.USER_INFO_V1
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MealieDataSourceWrapperTest : BaseUnitTest() {

    @MockK
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var v0Source: MealieDataSourceV0

    @MockK(relaxUnitFun = true)
    lateinit var v1Source: MealieDataSourceV1

    private val modelMapper: ModelMapper = ModelMapperImpl()

    lateinit var subject: MealieDataSourceWrapper

    @Before
    override fun setUp() {
        super.setUp()
        subject = MealieDataSourceWrapper(serverInfoRepo, v0Source, v1Source, modelMapper)
        coEvery { v0Source.requestUserInfo() } returns USER_INFO_V0
        coEvery { v1Source.requestUserInfo() } returns USER_INFO_V1
    }

    @Test
    fun `when server version v1 expect requestRecipeInfo to call v1`() = runTest {
        val slug = "porridge"
        coEvery { v1Source.requestRecipeInfo(eq(slug)) } returns PORRIDGE_RECIPE_RESPONSE_V1
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        val actual = subject.requestRecipeInfo(slug)

        coVerify { v1Source.requestRecipeInfo(eq(slug)) }

        assertThat(actual).isEqualTo(PORRIDGE_FULL_RECIPE_INFO)
    }

    @Test
    fun `when server version v1 expect requestRecipes to call v1`() = runTest {
        coEvery {
            v1Source.requestRecipes(any(), any())
        } returns listOf(PORRIDGE_RECIPE_SUMMARY_RESPONSE_V1)
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        val actual = subject.requestRecipes(40, 10)

        val page = 5 // 0-9 (1), 10-19 (2), 20-29 (3), 30-39 (4), 40-49 (5)
        val perPage = 10
        coVerify {
            v1Source.requestRecipes(eq(page), eq(perPage))
        }

        assertThat(actual).isEqualTo(listOf(RECIPE_SUMMARY_PORRIDGE_V1))
    }

    @Test
    fun `when server version v0 expect requestRecipes to call v0`() = runTest {
        coEvery {
            v0Source.requestRecipes(any(), any())
        } returns listOf(PORRIDGE_RECIPE_SUMMARY_RESPONSE_V0)
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        val start = 40
        val limit = 10
        val actual = subject.requestRecipes(start, limit)

        coVerify {
            v0Source.requestRecipes(eq(start), eq(limit))
        }

        assertThat(actual).isEqualTo(listOf(RECIPE_SUMMARY_PORRIDGE_V0))
    }

    @Test(expected = IOException::class)
    fun `when request fails expect addRecipe to rethrow`() = runTest {
        coEvery { v0Source.addRecipe(any()) } throws IOException()
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER
        subject.addRecipe(PORRIDGE_ADD_RECIPE_INFO)
    }

    @Test
    fun `when server version v0 expect addRecipe to call v0`() = runTest {
        val slug = "porridge"

        coEvery { v0Source.addRecipe(any()) } returns slug
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        val actual = subject.addRecipe(PORRIDGE_ADD_RECIPE_INFO)

        coVerify {
            v0Source.addRecipe(
                eq(PORRIDGE_ADD_RECIPE_REQUEST_V0),
            )
        }

        assertThat(actual).isEqualTo(slug)
    }

    @Test
    fun `when server version v1 expect addRecipe to call v1`() = runTest {
        val slug = "porridge"

        coEvery { v1Source.createRecipe(any()) } returns slug
        coEvery {
            v1Source.updateRecipe(any(), any())
        } returns PORRIDGE_RECIPE_RESPONSE_V1
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        coEvery { authRepo.getAuthHeader() } returns TEST_AUTH_HEADER

        val actual = subject.addRecipe(PORRIDGE_ADD_RECIPE_INFO)

        coVerifySequence {
            v1Source.createRecipe(
                eq(PORRIDGE_CREATE_RECIPE_REQUEST_V1),
            )

            v1Source.updateRecipe(
                eq(slug),
                eq(PORRIDGE_UPDATE_RECIPE_REQUEST_V1),
            )
        }

        assertThat(actual).isEqualTo(slug)
    }

    @Test
    fun `when remove favorite recipe info with v0 expect correct sequence`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = false)
        coVerify {
            v0Source.requestUserInfo()
            v0Source.removeFavoriteRecipe(eq(3), eq("cake"))
        }
    }

    @Test
    fun `when remove favorite recipe info with v1 expect correct sequence`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = false)
        coVerify {
            v1Source.requestUserInfo()
            v1Source.removeFavoriteRecipe(eq("userId"), eq("cake"))
        }
    }

    @Test
    fun `when add favorite recipe info with v0 expect correct sequence`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = true)
        coVerify {
            v0Source.requestUserInfo()
            v0Source.addFavoriteRecipe(eq(3), eq("cake"))
        }
    }

    @Test
    fun `when add favorite recipe info with v1 expect correct sequence`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        subject.updateIsRecipeFavorite(recipeSlug = "cake", isFavorite = true)
        coVerify {
            v1Source.requestUserInfo()
            v1Source.addFavoriteRecipe(eq("userId"), eq("cake"))
        }
    }

    @Test
    fun `when get favorite recipes with v1 expect correct call`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        subject.getFavoriteRecipes()
        coVerify { v1Source.requestUserInfo() }
    }

    @Test
    fun `when get favorite recipes with v0 expect correct call`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        subject.getFavoriteRecipes()
        coVerify { v0Source.requestUserInfo() }
    }

    @Test
    fun `when get favorite recipes with v1 expect correct result`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V1
        assertThat(subject.getFavoriteRecipes()).isEqualTo(FAVORITE_RECIPES_LIST)
    }

    @Test
    fun `when get favorite recipes with v0 expect correct result`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION_V0
        assertThat(subject.getFavoriteRecipes()).isEqualTo(FAVORITE_RECIPES_LIST)
    }
}