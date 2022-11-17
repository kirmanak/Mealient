package gq.kirmanak.mealient.ui.activity

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Test

class MainActivityViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: AuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var disclaimerStorage: DisclaimerStorage

    @MockK(relaxUnitFun = true)
    lateinit var serverInfoRepo: ServerInfoRepo

    @MockK(relaxUnitFun = true)
    lateinit var recipeRepo: RecipeRepo

    private lateinit var subject: MainActivityViewModel

    @Before
    override fun setUp() {
        super.setUp()
        every { authRepo.isAuthorizedFlow } returns emptyFlow()
        coEvery { disclaimerStorage.isDisclaimerAccepted() } returns true
        coEvery { serverInfoRepo.getUrl() } returns TEST_BASE_URL
        subject = MainActivityViewModel(
            authRepo = authRepo,
            logger = logger,
            disclaimerStorage = disclaimerStorage,
            serverInfoRepo = serverInfoRepo,
            recipeRepo = recipeRepo,
        )
    }

    @Test
    fun `when onSearchQuery with query expect call to recipe repo`() {
        subject.onSearchQuery("query")
        verify { recipeRepo.updateNameQuery("query") }
    }

    @Test
    fun `when onSearchQuery with null expect call to recipe repo`() {
        subject.onSearchQuery("query")
        subject.onSearchQuery(null)
        verify { recipeRepo.updateNameQuery(null) }
    }
}