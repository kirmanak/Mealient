package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoStorage
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_AUTH_HEADER
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.RecipeImplTestData.GET_CAKE_RESPONSE
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MealieDataSourceV0WrapperTest {

    @MockK
    lateinit var serverInfoStorage: ServerInfoStorage

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK
    lateinit var mealieDataSourceV0: MealieDataSourceV0

    lateinit var subject: MealieDataSourceWrapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MealieDataSourceWrapper(serverInfoStorage, authRepo, mealieDataSourceV0)
    }

    @Test
    fun `when withAuthHeader fails with Unauthorized then invalidates auth`() = runTest {
        coEvery { serverInfoStorage.requireBaseURL() } returns TEST_BASE_URL
        coEvery { authRepo.getAuthHeader() } returns null andThen TEST_AUTH_HEADER
        coEvery {
            mealieDataSourceV0.requestRecipeInfo(eq(TEST_BASE_URL), isNull(), eq("cake"))
        } throws NetworkError.Unauthorized(IOException())
        coEvery {
            mealieDataSourceV0.requestRecipeInfo(eq(TEST_BASE_URL), eq(TEST_AUTH_HEADER), eq("cake"))
        } returns GET_CAKE_RESPONSE
        subject.requestRecipeInfo("cake")
        coVerifyAll {
            authRepo.getAuthHeader()
            authRepo.invalidateAuthHeader()
            authRepo.getAuthHeader()
        }
    }

}