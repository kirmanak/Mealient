package gq.kirmanak.mealient.data.recipes.impl

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.baseurl.ServerInfoStorage
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RecipeImageUrlProviderImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: RecipeImageUrlProvider

    @Inject
    lateinit var serverInfoStorage: ServerInfoStorage

    @Before
    fun setUp() {
        prepareBaseURL("https://google.com/")
    }

    @Test
    fun `when url has slash then generated doesn't add new`() = runTest {
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isEqualTo("https://google.com/api/media/recipes/cake/images/original.webp")
    }

    @Test
    fun `when url doesn't have slash then generated adds new`() = runTest {
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isEqualTo("https://google.com/api/media/recipes/cake/images/original.webp")
    }

    @Test
    fun `when url is null then generated is null`() = runTest {
        prepareBaseURL(null)
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when url is blank then generated is null`() = runTest {
        prepareBaseURL("  ")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when url is empty then generated is null`() = runTest {
        prepareBaseURL("")
        val actual = subject.generateImageUrl("cake")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is empty then generated is null`() = runTest {
        val actual = subject.generateImageUrl("")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is blank then generated is null`() = runTest {
        val actual = subject.generateImageUrl("  ")
        assertThat(actual).isNull()
    }

    @Test
    fun `when slug is null then generated is null`() = runTest {
        val actual = subject.generateImageUrl(null)
        assertThat(actual).isNull()
    }

    private fun prepareBaseURL(baseURL: String?) = runBlocking {
        serverInfoStorage.storeBaseURL(baseURL)
    }
}