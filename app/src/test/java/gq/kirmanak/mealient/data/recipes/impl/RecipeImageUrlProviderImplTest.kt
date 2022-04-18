package gq.kirmanak.mealient.data.recipes.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeImageUrlProviderImplTest {

    lateinit var subject: RecipeImageUrlProvider

    @MockK
    lateinit var baseURLStorage: BaseURLStorage

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = RecipeImageUrlProviderImpl(baseURLStorage)
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

    private fun prepareBaseURL(baseURL: String?) {
        coEvery { baseURLStorage.getBaseURL() } returns baseURL
    }
}