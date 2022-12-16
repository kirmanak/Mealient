package gq.kirmanak.mealient.ui.share

import androidx.lifecycle.asFlow
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.share.ShareRecipeRepo
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.ui.OperationUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareRecipeViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var shareRecipeRepo: ShareRecipeRepo

    lateinit var subject: ShareRecipeViewModel

    @Before
    override fun setUp() {
        super.setUp()
        subject = ShareRecipeViewModel(
            shareRecipeRepo = shareRecipeRepo,
            logger = logger,
        )
    }

    @Test
    fun `when repo throws expect saveRecipeByURL to update saveResult`() {
        coEvery { shareRecipeRepo.saveRecipeByURL(any()) } throws RuntimeException()
        subject.saveRecipeByURL("")
        assertThat(subject.saveResult.value).isInstanceOf(OperationUiState.Failure::class.java)
    }

    @Test
    fun `when repo returns result expect saveResult to show progress before result`() = runTest {
        val deferredActual = async(Dispatchers.Default) {
            subject.saveResult.asFlow().take(3).toList(mutableListOf())
        }
        coEvery { shareRecipeRepo.saveRecipeByURL(any()) } returns "result"
        subject.saveRecipeByURL("")
        val actual = deferredActual.await()
        assertThat(actual).containsExactly(
            OperationUiState.Initial<String>(),
            OperationUiState.Progress<String>(),
            OperationUiState.Success("result"),
        ).inOrder()
    }

    @Test
    fun `when url is given expect saveRecipeByURL to pass it to repo`() = runTest {
        coEvery { shareRecipeRepo.saveRecipeByURL(any()) } returns "result"
        subject.saveRecipeByURL("https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/")
        coVerify { shareRecipeRepo.saveRecipeByURL(eq("https://www.allrecipes.com/recipe/215447/dads-leftover-turkey-pot-pie/")) }
    }
}