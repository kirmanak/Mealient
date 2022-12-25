package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.database.CAKE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.database.PORRIDGE_RECIPE_SUMMARY_ENTITY
import gq.kirmanak.mealient.database.TEST_RECIPE_SUMMARY_ENTITIES
import gq.kirmanak.mealient.database.recipe.RecipeStorage
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class RecipePagingSourceFactoryImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipePagingSourceFactory

    @Inject
    lateinit var storage: RecipeStorage

    @Test
    fun `when query is ca expect cake only is returned`() = runTest {
        storage.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.setQuery("ca")
        assertThat(queryRecipes()).isEqualTo(listOf(CAKE_RECIPE_SUMMARY_ENTITY))
    }

    @Test
    fun `when query is po expect porridge only is returned`() = runTest {
        storage.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.setQuery("po")
        assertThat(queryRecipes()).isEqualTo(listOf(PORRIDGE_RECIPE_SUMMARY_ENTITY))
    }

    @Test
    fun `when query is e expect cake and porridge are returned`() = runTest {
        storage.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.setQuery("e")
        assertThat(queryRecipes()).isEqualTo(TEST_RECIPE_SUMMARY_ENTITIES)
    }

    @Test
    fun `when query is null expect cake and porridge are returned`() = runTest {
        storage.saveRecipes(TEST_RECIPE_SUMMARY_ENTITIES)
        subject.setQuery(null)
        assertThat(queryRecipes()).isEqualTo(TEST_RECIPE_SUMMARY_ENTITIES)
    }

    private suspend fun queryRecipes(): List<RecipeSummaryEntity> {
        val loadParam = PagingSource.LoadParams.Refresh<Int>(null, Int.MAX_VALUE, false)
        val loadResult = subject.invoke().load(loadParam)
        return (loadResult as PagingSource.LoadResult.Page).data
    }

}

