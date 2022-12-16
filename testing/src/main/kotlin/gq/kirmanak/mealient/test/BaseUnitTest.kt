package gq.kirmanak.mealient.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gq.kirmanak.mealient.architecture.configuration.AppDispatchers
import gq.kirmanak.mealient.logging.Logger
import io.mockk.MockKAnnotations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.Timeout

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseUnitTest {

    @get:Rule(order = 0)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val timeoutRule: Timeout = Timeout.seconds(10)

    protected val logger: Logger = FakeLogger()

    lateinit var dispatchers: AppDispatchers

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        dispatchers = object : AppDispatchers {
            override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
            override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
            override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
            override val unconfined: CoroutineDispatcher = UnconfinedTestDispatcher()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}