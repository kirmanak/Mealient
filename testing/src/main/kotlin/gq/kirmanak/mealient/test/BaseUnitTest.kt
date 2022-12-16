package gq.kirmanak.mealient.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gq.kirmanak.mealient.architecture.configuration.AppDispatchers
import gq.kirmanak.mealient.logging.Logger
import io.mockk.MockKAnnotations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    protected val logger: Logger = FakeLogger()

    lateinit var dispatchers: AppDispatchers

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        dispatchers = object : AppDispatchers {
            override val io: CoroutineDispatcher = StandardTestDispatcher()
            override val main: CoroutineDispatcher = StandardTestDispatcher()
            override val default: CoroutineDispatcher = StandardTestDispatcher()
            override val unconfined: CoroutineDispatcher = StandardTestDispatcher()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}