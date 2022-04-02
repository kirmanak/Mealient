package gq.kirmanak.mealient.ui.disclaimer

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class DisclaimerViewModelTest {
    @MockK(relaxUnitFun = true)
    lateinit var storage: DisclaimerStorage

    lateinit var subject: DisclaimerViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = DisclaimerViewModel(storage)
    }

    @Test
    fun `when tickerFlow 3 seconds then sends count every 3 seconds`() = runTest {
        subject.tickerFlow(3, TimeUnit.SECONDS).take(10).collect {
            assertThat(it * 3000).isEqualTo(currentTime)
        }
    }

    @Test
    fun `when tickerFlow 500 ms then sends count every 500 ms`() = runTest {
        subject.tickerFlow(500, TimeUnit.MILLISECONDS).take(10).collect {
            assertThat(it * 500).isEqualTo(currentTime)
        }
    }
}