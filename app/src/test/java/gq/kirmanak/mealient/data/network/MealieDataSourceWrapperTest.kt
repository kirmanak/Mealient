package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_SERVER_VERSION
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MealieDataSourceWrapperTest {

    @MockK
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK
    lateinit var v0Source: MealieDataSourceV0

    @MockK
    lateinit var v1Source: MealieDataSourceV1

    lateinit var subject: MealieDataSourceWrapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MealieDataSourceWrapper(serverInfoRepo, authRepo, v0Source, v1Source)
    }

    @Test
    fun `when withAuthHeader fails with Unauthorized then invalidates auth`() = runTest {
        coEvery { serverInfoRepo.getVersion() } returns TEST_SERVER_VERSION
        coEvery { serverInfoRepo.requireUrl() } returns TEST_BASE_URL
        coEvery { authRepo.getAuthHeader() } returns null andThen TEST_AUTH_HEADER
        coEvery {
            v0Source.requestRecipeInfo(eq(TEST_BASE_URL), isNull(), eq("cake"))
        } throws NetworkError.Unauthorized(IOException())
        val successResponse = mockk<GetRecipeResponseV0>(relaxed = true)
        coEvery {
            v0Source.requestRecipeInfo(eq(TEST_BASE_URL), eq(TEST_AUTH_HEADER), eq("cake"))
        } returns successResponse
        subject.requestRecipeInfo("cake")
        coVerifyAll {
            authRepo.getAuthHeader()
            authRepo.invalidateAuthHeader()
            authRepo.getAuthHeader()
        }
    }

}