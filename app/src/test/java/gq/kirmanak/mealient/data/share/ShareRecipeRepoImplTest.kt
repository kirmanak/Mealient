package gq.kirmanak.mealient.data.share

import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareRecipeRepoImplTest : BaseUnitTest() {


    @MockK(relaxUnitFun = true)
    lateinit var parseRecipeDataSource: ParseRecipeDataSource

    lateinit var subject: ShareRecipeRepo

    override fun setUp() {
        super.setUp()
        subject = ShareRecipeRepoImpl(logger, parseRecipeDataSource)
        coEvery { parseRecipeDataSource.parseRecipeFromURL(any()) } returns ""
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when url is empty expect saveRecipeByURL throws Exception`() = runTest {
        subject.saveRecipeByURL("")
    }

    @Test
    fun `when url is correct expect saveRecipeByURL saves it`() = runTest {
        subject.saveRecipeByURL("https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/")
        val expected = ParseRecipeURLInfo(
            url = "https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/",
            includeTags = true
        )
        coVerify { parseRecipeDataSource.parseRecipeFromURL(eq(expected)) }
    }

    @Test
    fun `when url has prefix expect saveRecipeByURL removes it`() = runTest {
        subject.saveRecipeByURL("My favorite recipe: https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/")
        val expected = ParseRecipeURLInfo(
            url = "https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/",
            includeTags = true
        )
        coVerify { parseRecipeDataSource.parseRecipeFromURL(eq(expected)) }
    }

    @Test
    fun `when url has suffix expect saveRecipeByURL removes it`() = runTest {
        subject.saveRecipeByURL("https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/ is my favorite recipe")
        val expected = ParseRecipeURLInfo(
            url = "https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie",
            includeTags = true
        )
        coVerify { parseRecipeDataSource.parseRecipeFromURL(eq(expected)) }
    }

    @Test
    fun `when url has prefix and suffix expect saveRecipeByURL removes them`() = runTest {
        subject.saveRecipeByURL("Actually, https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/ is my favorite recipe")
        val expected = ParseRecipeURLInfo(
            url = "https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie",
            includeTags = true
        )
        coVerify { parseRecipeDataSource.parseRecipeFromURL(eq(expected)) }
    }
}