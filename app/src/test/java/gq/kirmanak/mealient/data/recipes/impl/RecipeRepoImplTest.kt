package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.InvalidatingPagingSourceFactory
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.RecipeImplTestData.FULL_CAKE_INFO_ENTITY
import gq.kirmanak.mealient.test.RecipeImplTestData.GET_CAKE_RESPONSE
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepoImplTest {

    @MockK(relaxUnitFun = true)
    lateinit var storage: RecipeStorage

    @MockK
    lateinit var dataSource: RecipeDataSource

    @MockK
    lateinit var remoteMediator: RecipesRemoteMediator

    @MockK
    lateinit var pagingSourceFactory: InvalidatingPagingSourceFactory<Int, RecipeSummaryEntity>

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    lateinit var subject: RecipeRepo

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = RecipeRepoImpl(remoteMediator, storage, pagingSourceFactory, dataSource, logger)
    }

    @Test
    fun `when loadRecipeInfo then loads recipe`() = runTest {
        coEvery { dataSource.requestRecipeInfo(eq("cake")) } returns GET_CAKE_RESPONSE
        coEvery { storage.queryRecipeInfo(eq("1")) } returns FULL_CAKE_INFO_ENTITY
        val actual = subject.loadRecipeInfo("1", "cake")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }

    @Test
    fun `when loadRecipeInfo then saves to DB`() = runTest {
        coEvery { dataSource.requestRecipeInfo(eq("cake")) } returns GET_CAKE_RESPONSE
        coEvery { storage.queryRecipeInfo(eq("1")) } returns FULL_CAKE_INFO_ENTITY
        subject.loadRecipeInfo("1", "cake")
        coVerify { storage.saveRecipeInfo(eq(GET_CAKE_RESPONSE)) }
    }

    @Test
    fun `when loadRecipeInfo with error then loads from DB`() = runTest {
        coEvery { dataSource.requestRecipeInfo(eq("cake")) } throws RuntimeException()
        coEvery { storage.queryRecipeInfo(eq("1")) } returns FULL_CAKE_INFO_ENTITY
        val actual = subject.loadRecipeInfo("1", "cake")
        assertThat(actual).isEqualTo(FULL_CAKE_INFO_ENTITY)
    }
}