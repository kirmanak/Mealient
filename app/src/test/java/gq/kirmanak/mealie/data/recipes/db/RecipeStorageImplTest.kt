package gq.kirmanak.mealie.data.recipes.db

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealie.data.MealieDb
import gq.kirmanak.mealie.data.auth.impl.HiltRobolectricTest
import gq.kirmanak.mealie.data.recipes.network.GetRecipeSummaryResponse
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RecipeStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipeStorageImpl

    @Inject
    lateinit var mealieDb: MealieDb

    @Test
    fun `when saveRecipes then saves tags`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actualTags = mealieDb.recipeDao().queryAllTags()
        assertThat(actualTags).containsExactly(
            TagEntity(localId = 1, name = "gluten"),
            TagEntity(localId = 2, name = "allergic"),
            TagEntity(localId = 3, name = "milk")
        )
    }

    @Test
    fun `when saveRecipes then saves categories`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllCategories()
        assertThat(actual).containsExactly(
            CategoryEntity(localId = 1, name = "dessert"),
            CategoryEntity(localId = 2, name = "tasty"),
            CategoryEntity(localId = 3, name = "porridge")
        )
    }

    @Test
    fun `when saveRecipes then saves recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actualTags = mealieDb.recipeDao().queryAllRecipes()
        assertThat(actualTags).containsExactly(
            RecipeEntity(
                localId = 1,
                remoteId = 1,
                name = "Cake",
                slug = "cake",
                image = "86",
                description = "A tasty cake",
                rating = 4,
                dateAdded = LocalDate.parse("2021-11-13"),
                dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13")
            ),
            RecipeEntity(
                localId = 2,
                remoteId = 2,
                name = "Porridge",
                slug = "porridge",
                image = "89",
                description = "A tasty porridge",
                rating = 5,
                dateAdded = LocalDate.parse("2021-11-12"),
                dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
            )
        )
    }

    @Test
    fun `when saveRecipes then saves category recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllCategoryRecipes()
        assertThat(actual).containsExactly(
            CategoryRecipeEntity(categoryId = 1, recipeId = 1),
            CategoryRecipeEntity(categoryId = 2, recipeId = 1),
            CategoryRecipeEntity(categoryId = 3, recipeId = 2),
            CategoryRecipeEntity(categoryId = 2, recipeId = 2)
        )
    }

    @Test
    fun `when saveRecipes then saves tag recipes`(): Unit = runBlocking {
        subject.saveRecipes(TEST_RECIPE_SUMMARIES)
        val actual = mealieDb.recipeDao().queryAllTagRecipes()
        assertThat(actual).containsExactly(
            TagRecipeEntity(tagId = 1, recipeId = 1),
            TagRecipeEntity(tagId = 2, recipeId = 1),
            TagRecipeEntity(tagId = 3, recipeId = 2),
            TagRecipeEntity(tagId = 1, recipeId = 2),
        )
    }

    companion object {
        private val RECIPE_SUMMARY_CAKE = GetRecipeSummaryResponse(
            remoteId = 1,
            name = "Cake",
            slug = "cake",
            image = "86",
            description = "A tasty cake",
            recipeCategories = listOf("dessert", "tasty"),
            tags = listOf("gluten", "allergic"),
            rating = 4,
            dateAdded = LocalDate.parse("2021-11-13"),
            dateUpdated = LocalDateTime.parse("2021-11-13T15:30:13"),
        )

        private val RECIPE_SUMMARY_PORRIDGE = GetRecipeSummaryResponse(
            remoteId = 2,
            name = "Porridge",
            slug = "porridge",
            image = "89",
            description = "A tasty porridge",
            recipeCategories = listOf("porridge", "tasty"),
            tags = listOf("gluten", "milk"),
            rating = 5,
            dateAdded = LocalDate.parse("2021-11-12"),
            dateUpdated = LocalDateTime.parse("2021-10-13T17:35:23"),
        )

        private val TEST_RECIPE_SUMMARIES = listOf(RECIPE_SUMMARY_CAKE, RECIPE_SUMMARY_PORRIDGE)
    }
}