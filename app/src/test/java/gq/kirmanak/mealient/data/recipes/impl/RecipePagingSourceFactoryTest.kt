package gq.kirmanak.mealient.data.recipes.impl

import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RecipePagingSourceFactoryTest : HiltRobolectricTest() {
    @Inject
    lateinit var storage: RecipeStorage
    lateinit var subject: RecipePagingSourceFactory

    @Before
    fun setUp() {
        subject = RecipePagingSourceFactory(storage)
    }

    @Test
    fun `when modifying concurrently then doesn't throw`(): Unit = runBlocking {
        (0..100).map {
            async(Dispatchers.Default) {
                for (i in 0..100) {
                    subject.invalidate()
                    subject.invoke()
                }
            }
        }.awaitAll()
    }

}