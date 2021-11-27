package gq.kirmanak.mealient.data.recipes.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_TOKEN
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_URL
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RecipeImageLoaderImplTest : HiltRobolectricTest() {
    @Inject
    lateinit var subject: RecipeImageLoaderImpl

    @Inject
    lateinit var authStorage: AuthStorage

    @Test
    fun `when url has slash then generated doesn't add new`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, "https://google.com/")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isEqualTo("https://google.com/api/media/recipes/cake/images/original.webp")
    }

    @Test
    fun `when url doesn't have slash then generated adds new`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, "https://google.com")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isEqualTo("https://google.com/api/media/recipes/cake/images/original.webp")
    }

    @Test
    fun `when url is null then generated is null`() = runBlocking {
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when url is blank then generated is null`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, "  ")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when url is empty then generated is null`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, "")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is empty then generated is null`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, TEST_URL)
        val actual = subject.generateImageUrl("")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is blank then generated is null`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, TEST_URL)
        val actual = subject.generateImageUrl("  ")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is null then generated is null`() = runBlocking {
        authStorage.storeAuthData(TEST_TOKEN, TEST_URL)
        val actual = subject.generateImageUrl(null)
        assertThat(actual).isNull()
    }
}