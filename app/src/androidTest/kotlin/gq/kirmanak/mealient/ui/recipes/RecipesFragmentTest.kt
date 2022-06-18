package gq.kirmanak.mealient.ui.recipes

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RecipesFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun firstTest() {
        launchFragmentInHiltContainer<RecipesFragment>()
        Thread.sleep(10000)
    }
}