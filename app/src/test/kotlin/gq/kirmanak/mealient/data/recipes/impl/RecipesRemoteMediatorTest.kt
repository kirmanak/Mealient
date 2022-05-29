package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.*
import androidx.paging.LoadType.*
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.network.NetworkError.Unauthorized
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.test.RecipeImplTestData.TEST_RECIPE_SUMMARIES
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@OptIn(ExperimentalPagingApi::class)
class RecipesRemoteMediatorTest {
    private val pagingConfig = PagingConfig(
        pageSize = 2,
        prefetchDistance = 5,
        enablePlaceholders = false
    )

    lateinit var subject: RecipesRemoteMediator

    @MockK(relaxUnitFun = true)
    lateinit var storage: RecipeStorage

    @MockK
    lateinit var dataSource: RecipeDataSource

    @MockK(relaxUnitFun = true)
    lateinit var pagingSourceFactory: InvalidatingPagingSourceFactory<Int, RecipeSummaryEntity>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = RecipesRemoteMediator(storage, dataSource, pagingSourceFactory)
    }

    @Test
    fun `when first load with refresh successful then result success`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } returns TEST_RECIPE_SUMMARIES
        val result = subject.load(REFRESH, pagingState())
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
    }

    @Test
    fun `when first load with refresh successful then end is reached`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } returns TEST_RECIPE_SUMMARIES
        val result = subject.load(REFRESH, pagingState())
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `when first load with refresh successful then invalidate called`() = runTest {
        coEvery { dataSource.requestRecipes(any(), any()) } returns TEST_RECIPE_SUMMARIES
        subject.load(REFRESH, pagingState())
        verify { pagingSourceFactory.invalidate() }
    }

    @Test
    fun `when first load with refresh successful then recipes stored`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } returns TEST_RECIPE_SUMMARIES
        subject.load(REFRESH, pagingState())
        coVerify { storage.refreshAll(eq(TEST_RECIPE_SUMMARIES)) }
    }

    @Test
    fun `when load state prepend then success`() = runTest {
        val result = subject.load(PREPEND, pagingState())
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
    }

    @Test
    fun `when load state prepend then end is reached`() = runTest {
        val result = subject.load(PREPEND, pagingState())
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `when load successful then lastRequestEnd updated`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } returns TEST_RECIPE_SUMMARIES
        subject.load(REFRESH, pagingState())
        val actual = subject.lastRequestEnd
        assertThat(actual).isEqualTo(2)
    }

    @Test
    fun `when load fails then lastRequestEnd still 0`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } throws Unauthorized(RuntimeException())
        subject.load(REFRESH, pagingState())
        val actual = subject.lastRequestEnd
        assertThat(actual).isEqualTo(0)
    }

    @Test
    fun `when load fails then result is error`() = runTest {
        coEvery { dataSource.requestRecipes(eq(0), eq(6)) } throws Unauthorized(RuntimeException())
        val actual = subject.load(REFRESH, pagingState())
        assertThat(actual).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
    }

    @Test
    fun `when refresh then request params correct`() = runTest {
        coEvery { dataSource.requestRecipes(any(), any()) } throws Unauthorized(RuntimeException())
        subject.load(REFRESH, pagingState())
        coVerify { dataSource.requestRecipes(eq(0), eq(6)) }
    }

    @Test
    fun `when append then request params correct`() = runTest {
        coEvery { dataSource.requestRecipes(any(), any()) } returns TEST_RECIPE_SUMMARIES
        subject.load(REFRESH, pagingState())
        subject.load(APPEND, pagingState())
        coVerify {
            dataSource.requestRecipes(eq(0), eq(6))
            dataSource.requestRecipes(eq(2), eq(2))
        }
    }

    @Test
    fun `when append fails then recipes aren't removed`() = runTest {
        coEvery { dataSource.requestRecipes(any(), any()) } returns TEST_RECIPE_SUMMARIES
        subject.load(REFRESH, pagingState())
        coEvery { dataSource.requestRecipes(any(), any()) } throws Unauthorized(RuntimeException())
        subject.load(APPEND, pagingState())
        coVerify {
            storage.refreshAll(TEST_RECIPE_SUMMARIES)
        }
    }

    private fun pagingState(
        pages: List<PagingSource.LoadResult.Page<Int, RecipeSummaryEntity>> = emptyList(),
        anchorPosition: Int? = null
    ): PagingState<Int, RecipeSummaryEntity> = PagingState(
        pages = pages,
        anchorPosition = anchorPosition,
        config = pagingConfig,
        leadingPlaceholderCount = 0
    )
}