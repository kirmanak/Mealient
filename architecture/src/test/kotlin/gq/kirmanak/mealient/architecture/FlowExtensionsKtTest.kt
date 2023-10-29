package gq.kirmanak.mealient.architecture

import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.test.BaseUnitTest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FlowExtensionsKtTest : BaseUnitTest() {

    @Test
    fun `when flow has an update then valueUpdatesOnly sends updated value`() = runTest {
        val flow = flowOf(1, 2)
        assertThat(flow.valueUpdatesOnly().toList()).isEqualTo(listOf(2))
    }

    @Test
    fun `when flow has repeated values then valueUpdatesOnly sends updated value`() = runTest {
        val flow = flowOf(1, 1, 1, 2)
        assertThat(flow.valueUpdatesOnly().toList()).isEqualTo(listOf(2))
    }

    @Test
    fun `when flow has one value then valueUpdatesOnly is empty`() = runTest {
        val flow = flowOf(1)
        assertThat(flow.valueUpdatesOnly().toList()).isEmpty()
    }

    @Test
    fun `when flow has two updates then valueUpdatesOnly sends both`() = runTest {
        val flow = flowOf(1, 2, 1)
        assertThat(flow.valueUpdatesOnly().toList()).isEqualTo(listOf(2, 1))
    }

    @Test
    fun `when flow has three updates then valueUpdatesOnly sends all`() = runTest {
        val flow = flowOf(1, 2, 1, 3)
        assertThat(flow.valueUpdatesOnly().toList()).isEqualTo(listOf(2, 1, 3))
    }
}