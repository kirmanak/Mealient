package gq.kirmanak.mealient

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.screen.DisclaimerScreen
import gq.kirmanak.mealient.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FirstSetUpTest : TestCase() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainActivityRule = activityScenarioRule<MainActivity>()

    @Test
    fun test() = run {
        step("Ensure button is disabled") {
            DisclaimerScreen {
                okayButton {
                    isVisible()
                    isDisabled()
                }
            }
        }
    }
}