package gq.kirmanak.mealient.data.add.impl

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.logging.Logger
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeDataSourceImplTest {

    @MockK
    lateinit var serviceProvider: ServiceFactory<AddRecipeService>

    @MockK
    lateinit var service: AddRecipeService

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    lateinit var subject: AddRecipeDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { serviceProvider.provideService(any()) } returns service
        subject = AddRecipeDataSourceImpl(serviceProvider, logger)
    }

    @Test(expected = NetworkError.NotMealie::class)
    fun `when addRecipe fails then maps error`() = runTest {
        coEvery { service.addRecipe(any()) } throws SerializationException()
        subject.addRecipe(AddRecipeRequest())
    }

    @Test
    fun `when addRecipe succeeds then returns response`() = runTest {
        coEvery { service.addRecipe(any()) } returns "response"
        assertThat(subject.addRecipe(AddRecipeRequest())).isEqualTo("response")
    }

}