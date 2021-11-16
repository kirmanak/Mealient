package gq.kirmanak.mealie.data.recipes.impl

import androidx.paging.*
import androidx.paging.LoadType.*
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.MealieDb
import gq.kirmanak.mealie.data.auth.AuthRepo
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealie.data.auth.impl.AuthImplTestData.enqueueSuccessfulAuthResponse
import gq.kirmanak.mealie.data.auth.impl.MockServerTest
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.CAKE_RECIPE_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.PORRIDGE_RECIPE_ENTITY
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.TEST_RECIPE_ENTITIES
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.enqueueSuccessfulRecipeSummaryResponse
import gq.kirmanak.mealie.data.recipes.RecipeImplTestData.enqueueUnsuccessfulRecipeSummaryResponse
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalPagingApi
@HiltAndroidTest
class RecipesRemoteMediatorTest : MockServerTest() {
    private val pagingConfig = PagingConfig(
        pageSize = 2,
        prefetchDistance = 5,
        enablePlaceholders = false
    )

    @Inject
    lateinit var subject: RecipesRemoteMediator

    @Inject
    lateinit var authRepo: AuthRepo

    @Inject
    lateinit var mealieDb: MealieDb

    @Before
    fun authenticate(): Unit = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        authRepo.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        mockServer.takeRequest()
    }

    @Test
    fun `when first load with refresh successful then result success`(): Unit = runBlocking {
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        val result = subject.load(REFRESH, pagingState())
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
    }

    @Test
    fun `when first load with refresh successful then recipes stored`(): Unit = runBlocking {
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        val actual = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actual).containsExactly(CAKE_RECIPE_ENTITY, PORRIDGE_RECIPE_ENTITY)
    }

    @Test
    fun `when load state prepend then success`(): Unit = runBlocking {
        val result = subject.load(PREPEND, pagingState())
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
    }

    @Test
    fun `when load state prepend then end is reached`(): Unit = runBlocking {
        val result = subject.load(PREPEND, pagingState())
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `when load successful then lastRequestEnd updated`(): Unit = runBlocking {
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        val actual = subject.lastRequestEnd
        assertThat(actual).isEqualTo(2)
    }

    @Test
    fun `when load fails then lastRequestEnd still 0`(): Unit = runBlocking {
        mockServer.enqueueUnsuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        val actual = subject.lastRequestEnd
        assertThat(actual).isEqualTo(0)
    }

    @Test
    fun `when load fails then result is error`(): Unit = runBlocking {
        mockServer.enqueueUnsuccessfulRecipeSummaryResponse()
        val actual = subject.load(REFRESH, pagingState())
        assertThat(actual).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
    }

    @Test
    fun `when refresh then request params correct`(): Unit = runBlocking {
        mockServer.enqueueUnsuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        val actual = mockServer.takeRequest().path
        assertThat(actual).isEqualTo("/api/recipes/summary?start=0&limit=6")
    }

    @Test
    fun `when append then request params correct`(): Unit = runBlocking {
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        mockServer.takeRequest()
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        subject.load(APPEND, pagingState())
        val actual = mockServer.takeRequest().path
        assertThat(actual).isEqualTo("/api/recipes/summary?start=2&limit=2")
    }

    @Test
    fun `when append fails then recipes aren't removed`(): Unit = runBlocking {
        mockServer.enqueueSuccessfulRecipeSummaryResponse()
        subject.load(REFRESH, pagingState())
        mockServer.takeRequest()
        mockServer.enqueueUnsuccessfulRecipeSummaryResponse()
        subject.load(APPEND, pagingState())
        val actual = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actual).isEqualTo(TEST_RECIPE_ENTITIES)
    }

    private fun pagingState(
        pages: List<PagingSource.LoadResult.Page<Int, RecipeEntity>> = emptyList(),
        anchorPosition: Int? = null
    ): PagingState<Int, RecipeEntity> = PagingState(
        pages = pages,
        anchorPosition = anchorPosition,
        config = pagingConfig,
        leadingPlaceholderCount = 0
    )
}