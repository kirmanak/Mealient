package gq.kirmanak.mealient.data.test

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.test.AuthImplTestData.TEST_PASSWORD
import gq.kirmanak.mealient.data.test.AuthImplTestData.TEST_USERNAME
import gq.kirmanak.mealient.data.test.AuthImplTestData.enqueueSuccessfulAuthResponse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import javax.inject.Inject

abstract class MockServerWithAuthTest : MockServerTest() {
    @Inject
    lateinit var authRepo: AuthRepo

    @Before
    fun authenticate(): Unit = runBlocking {
        mockServer.enqueueSuccessfulAuthResponse()
        authRepo.authenticate(TEST_USERNAME, TEST_PASSWORD, serverUrl)
        mockServer.takeRequest()
    }
}