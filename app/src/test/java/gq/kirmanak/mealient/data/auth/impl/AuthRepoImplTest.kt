package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.service.auth.AccountManagerInteractor
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

class AuthRepoImplTest {

    @MockK
    lateinit var accountManagerInteractor: AccountManagerInteractor

    lateinit var subject: AuthRepoImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AuthRepoImpl(accountManagerInteractor)
    }

    // TODO write the actual tests
}
