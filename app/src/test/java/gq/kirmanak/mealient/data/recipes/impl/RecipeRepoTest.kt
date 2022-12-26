package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.LoadType
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.database.CAKE_BREAD_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.database.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealient.database.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.database.CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY
import gq.kirmanak.mealient.database.FULL_CAKE_INFO_ENTITY
import gq.kirmanak.mealient.database.MIX_CAKE_RECIPE_INSTRUCTION_ENTITY
import gq.kirmanak.mealient.database.recipe.RecipeStorage
import gq.kirmanak.mealient.datasource.NetworkError.Unauthorized
import gq.kirmanak.mealient.datasource_test.CAKE_FULL_RECIPE_INFO
import gq.kirmanak.mealient.model_mapper.ModelMapper
import gq.kirmanak.mealient.model_mapper.ModelMapperImpl
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepoTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var storage: RecipeStorage

    @MockK(relaxUnitFun = true)
    lateinit var dataSource: RecipeDataSource

    @MockK
    lateinit var remoteMediator: RecipesRemoteMediator

    @MockK(relaxUnitFun = true)
    lateinit var pagingSourceFactory: RecipePagingSourceFactory

    private val modelMapper: ModelMapper = ModelMapperImpl()

    lateinit var subject: RecipeRepo

    @Before
    override fun setUp() {
        super.setUp()
        subject = RecipeRepoImpl(
            remoteMediator,
            storage,
            pagingSourceFactory,
            dataSource,
            logger,
            modelMapper,
        )
    }

    @Test
    fun `when loadRecipeInfo expect return value from data source`() = runTest {
        coEvery { storage.queryRecipeInfo(eq("1")) } returns FULL_CAKE_INFO_ENTITY
        val actual = subject.loadRecipeInfo("1")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when refreshRecipeInfo expect call to storage`() = runTest {
        coEvery { dataSource.requestRecipeInfo(eq("cake")) } returns CAKE_FULL_RECIPE_INFO
        subject.refreshRecipeInfo("cake")
        coVerify {
            storage.saveRecipeInfo(
                eq(CAKE_RECIPE_ENTITY),
                eq(
                    listOf(
                        CAKE_SUGAR_RECIPE_INGREDIENT_ENTITY,
                        CAKE_BREAD_RECIPE_INGREDIENT_ENTITY
                    )
                ),
                eq(listOf(MIX_CAKE_RECIPE_INSTRUCTION_ENTITY, BAKE_CAKE_RECIPE_INSTRUCTION_ENTITY))
            )
        }
    }

    @Test
    fun `when clearLocalData expect call to storage`() = runTest {
        subject.clearLocalData()
        coVerify { storage.clearAllLocalData() }
    }

    @Test
    fun `when updateNameQuery expect sets query in paging source factory`() {
        subject.updateNameQuery("query")
        verify { pagingSourceFactory.setQuery("query") }
    }

    @Test
    fun `when remove favorite recipe expect correct sequence`() = runTest {
        coEvery { dataSource.getFavoriteRecipes() } returns listOf("porridge")
        subject.updateIsRecipeFavorite("cake", false)
        coVerify {
            dataSource.updateIsRecipeFavorite(eq("cake"), eq(false))
            dataSource.getFavoriteRecipes()
            storage.updateFavoriteRecipes(eq(listOf("porridge")))
            pagingSourceFactory.invalidate()
        }
    }

    @Test
    fun `when add favorite recipe expect correct sequence`() = runTest {
        coEvery { dataSource.getFavoriteRecipes() } returns listOf("porridge", "cake")
        subject.updateIsRecipeFavorite("porridge", true)
        coVerify {
            dataSource.updateIsRecipeFavorite(eq("porridge"), eq(true))
            dataSource.getFavoriteRecipes()
            storage.updateFavoriteRecipes(eq(listOf("porridge", "cake")))
            pagingSourceFactory.invalidate()
        }
    }

    @Test
    fun `when add favorite recipe fails expect no mediator call`() = runTest {
        coEvery {
            dataSource.updateIsRecipeFavorite(any(), any())
        } throws Unauthorized(IOException())
        subject.updateIsRecipeFavorite("porridge", true)
        coVerify(inverse = true) { dataSource.getFavoriteRecipes() }
    }

    @Test
    fun `when refresh recipes expect correct parameters`() = runTest {
        subject.refreshRecipes()
        coVerify { remoteMediator.updateRecipes(eq(0), eq(150), eq(LoadType.REFRESH)) }
    }

    @Test
    fun `when delete recipe expect correct sequence`() = runTest {
        subject.deleteRecipe(CAKE_RECIPE_SUMMARY_ENTITY)
        coVerifyOrder {
            dataSource.deleteRecipe(eq("cake"))
            storage.deleteRecipe(eq(CAKE_RECIPE_SUMMARY_ENTITY))
            pagingSourceFactory.invalidate()
        }
    }

    @Test
    fun `when delete recipe remotely fails expect it isn't deleted locally`() = runTest {
        coEvery { dataSource.deleteRecipe(any()) } throws Unauthorized(IOException())
        subject.deleteRecipe(CAKE_RECIPE_SUMMARY_ENTITY)
        coVerify(inverse = true) { storage.deleteRecipe(any()) }
    }
}