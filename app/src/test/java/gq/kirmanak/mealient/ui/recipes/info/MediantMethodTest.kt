package gq.kirmanak.mealient.ui.recipes.info

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MediantMethodTest(
    private val input: Triple<Double, Int, Boolean>,
    private val output: Triple<Int, Int, Int>,
) {

    @Test
    fun `when mediantMethod is called expect correct result`() {
        assertThat(input.first.mediantMethod(input.second, input.third)).isEqualTo(output)
    }

    companion object {
        @Parameters
        @JvmStatic
        fun parameters(): List<Array<Any>> {
            return listOf(
                arrayOf(Triple(0.333, 10, true), Triple(0, 1, 3)),
                arrayOf(Triple(0.333, 10, false), Triple(0, 1, 3)),
                arrayOf(Triple(0.333, 100, false), Triple(0, 1, 3)),
                arrayOf(Triple(0.333, 100, true), Triple(0, 1, 3)),
                arrayOf(Triple(1.5, 10, true), Triple(1, 1, 2)),
                arrayOf(Triple(1.5, 10, false), Triple(0, 3, 2)),
                arrayOf(Triple(0.4, 10, false), Triple(0, 2, 5)),
                arrayOf(Triple(0.41412412412412, 100, true), Triple(0, 41, 99)),
                arrayOf(Triple(8.98, 10, true), Triple(9, 0, 1)),
            )
        }
    }
}