package gq.kirmanak.mealient

import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.screen.BaseUrlScreen
import gq.kirmanak.mealient.screen.DisclaimerScreen
import gq.kirmanak.mealient.screen.RecipesListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class FirstSetUpTest : BaseTestCase() {

    @Before
    fun dispatchUrls() {
        mockWebServer.dispatch { url, _ ->
            if (url == "/api/app/about") versionV1Response else notFoundResponse
        }
    }

    @Test
    fun test() = run {
        step("Ensure button is disabled") {
            onComposeScreen<DisclaimerScreen>(mainActivityRule) {
                okayButtonText {
                    assertIsDisplayed()
                    assertTextContains(getResourceString(R.string.fragment_disclaimer_button_okay))
                }

                okayButton {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                }

                disclaimerText {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.fragment_disclaimer_main_text))
                }
            }
        }

        step("Close disclaimer screen") {
            onComposeScreen<DisclaimerScreen>(mainActivityRule) {
                okayButtonText {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.fragment_disclaimer_button_okay))
                }

                okayButton {
                    assertIsDisplayed()
                    assertIsEnabled()
                    performClick()
                }
            }
        }

        step("Enter mock server address and click proceed") {
            onComposeScreen<BaseUrlScreen>(mainActivityRule) {
                progressBar {
                    assertIsNotDisplayed()
                }
                urlInput {
                    assertIsDisplayed()
                    performTextInput(mockWebServer.url("/").toString())
                }
                urlInputLabel {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.fragment_authentication_input_hint_url))
                }
                proceedButtonText {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.fragment_base_url_save))
                }
                proceedButton {
                    assertIsDisplayed()
                    assertIsEnabled()
                    performClick()
                }
            }
        }

        step("Check that empty list of recipes is shown") {
            onComposeScreen<RecipesListScreen>(mainActivityRule) {
                errorText {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.fragment_recipes_load_failure_toast_no_reason))
                }
            }
        }
    }
}