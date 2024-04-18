package gq.kirmanak.mealient.shopping_lists.ui.details

import androidx.lifecycle.SavedStateHandle
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemRecipeReferenceResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.test.BaseUnitTest
import gq.kirmanak.mealient.ui.util.LoadingHelper
import gq.kirmanak.mealient.ui.util.LoadingHelperFactory
import gq.kirmanak.mealient.ui.util.LoadingState
import gq.kirmanak.mealient.ui.util.LoadingStateNoData
import gq.kirmanak.mealient.ui.util.LoadingStateWithData
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import java.io.IOException

internal class ShoppingListViewModelTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var shoppingListsRepo: ShoppingListsRepo

    @MockK(relaxUnitFun = true)
    lateinit var authRepo: ShoppingListsAuthRepo

    @MockK(relaxUnitFun = true)
    lateinit var loadingHelperFactory: LoadingHelperFactory

    @MockK(relaxUnitFun = true)
    lateinit var loadingHelper: LoadingHelper<ShoppingListData>

    lateinit var subject: ShoppingListViewModel

    private val loadingState = MutableStateFlow<LoadingState<ShoppingListData>>(
        LoadingStateNoData.InitialLoad
    )

    private val isAuthorized = MutableStateFlow(false)

    @Test
    fun `when view model is created then the list is refreshed`() {
        createViewModel()
        coVerify { loadingHelper.refresh() }
    }

    @Test
    fun `when user authenticates then the list is refreshed`() {
        createViewModel()
        isAuthorized.value = true
        coVerify {
            loadingHelper.refresh() // On create
            loadingHelper.refresh() // On authentication
        }
    }

    @Test
    fun `when refresh fails then snackbar is shown`() {
        val error = IOException()
        createViewModel(
            refreshResult = Result.failure(error)
        )
        assertSame(error, subject.errorToShowInSnackbar)
    }


    @Test
    fun `when refresh succeeds then no snackbar shown`() {
        createViewModel()
        assertNull(subject.errorToShowInSnackbar)
    }

    @Test
    fun `when loading starts then state is initial load`() {
        createViewModel()
        assertEquals(LoadingStateNoData.InitialLoad, subject.loadingState.value)
    }

    @Test
    fun `when loading succeeds then data is shown`() {
        createViewModel()
        loadingState.value = LoadingStateWithData.Success(shoppingListData)
        assertEquals(LoadingStateWithData.Success(shoppingListScreen), subject.loadingState.value)
    }

    @Test
    fun `when load data is requested then repo is queried`() = runTest {
        val lambdaSlot = slot<suspend () -> Result<ShoppingListData>>()
        createViewModel(
            lambdaSlot = lambdaSlot
        )
        val lambda = lambdaSlot.captured
        val actualResult = lambda()
        assertEquals(Result.success(shoppingListData), actualResult)
    }

    private fun createViewModel(
        shoppingListId: String = "shoppingListId",
        refreshResult: Result<ShoppingListData> = Result.success(shoppingListData),
        lambdaSlot: CapturingSlot<suspend () -> Result<ShoppingListData>> = slot<suspend () -> Result<ShoppingListData>>(),
    ) {
        val savedStateHandle = SavedStateHandle().also {
            it["shoppingListId"] = shoppingListId
        }
        every { loadingHelperFactory.create(any(), capture(lambdaSlot)) } returns loadingHelper
        every { loadingHelper.loadingState } returns loadingState
        coEvery { loadingHelper.refresh() } returns refreshResult
        every { authRepo.isAuthorizedFlow } returns isAuthorized
        coEvery { shoppingListsRepo.getFoods() } returns listOf(milkFood)
        coEvery { shoppingListsRepo.getUnits() } returns listOf(mlUnit)
        coEvery { shoppingListsRepo.getShoppingList(any()) } returns shoppingListResponse
        subject = ShoppingListViewModel(
            shoppingListsRepo = shoppingListsRepo,
            logger = logger,
            authRepo = authRepo,
            loadingHelperFactory = loadingHelperFactory,
            savedStateHandle = savedStateHandle
        )
    }
}


private val mlUnit = GetUnitResponse("ml", "")

private val milkFood = GetFoodResponse("Milk", "")

private val blackTeaBags = GetShoppingListItemResponse(
    id = "1",
    shoppingListId = "1",
    checked = false,
    position = 0,
    isFood = false,
    note = "Black tea bags",
    quantity = 30.0,
    unit = null,
    food = null,
    recipeReferences = listOf(
        GetShoppingListItemRecipeReferenceResponse(
            recipeId = "1",
            recipeQuantity = 1.0,
        ),
    ),
)

private val milk = GetShoppingListItemResponse(
    id = "2",
    shoppingListId = "1",
    checked = true,
    position = 0,
    isFood = true,
    note = "Cold",
    quantity = 500.0,
    unit = mlUnit,
    food = milkFood,
    recipeReferences = listOf(
        GetShoppingListItemRecipeReferenceResponse(
            recipeId = "1",
            recipeQuantity = 500.0,
        ),
    ),
)

private val shoppingListResponse = GetShoppingListResponse(
    id = "shoppingListId",
    groupId = "shoppingListGroupId",
    name = "shoppingListName",
    listItems = listOf(blackTeaBags, milk),
    recipeReferences = listOf()
)

private val shoppingListData = ShoppingListData(
    foods = listOf(milkFood),
    units = listOf(mlUnit),
    shoppingList = shoppingListResponse
)

private val shoppingListScreen = ShoppingListScreenState(
    name = "shoppingListName",
    listId = "shoppingListId",
    items = listOf(
        ShoppingListItemState.ExistingItem(
            item = blackTeaBags,
            isEditing = false
        ),
        ShoppingListItemState.ExistingItem(
            item = milk,
            isEditing = false
        )
    ),
    foods = listOf(milkFood),
    units = listOf(mlUnit)
)