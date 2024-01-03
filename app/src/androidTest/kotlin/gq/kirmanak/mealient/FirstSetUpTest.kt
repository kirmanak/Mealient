package gq.kirmanak.mealient

import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.screen.AuthenticationScreen
import gq.kirmanak.mealient.screen.BaseUrlScreen
import gq.kirmanak.mealient.screen.DisclaimerScreen
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
                okayButton {
                    assertIsNotEnabled()
                }

                okayButtonText {
                    assertTextContains(getResourceString(R.string.fragment_disclaimer_button_okay))
                }

                disclaimerText {
                    assertTextEquals(getResourceString(R.string.fragment_disclaimer_main_text))
                }
            }
        }

        step("Close disclaimer screen") {
            onComposeScreen<DisclaimerScreen>(mainActivityRule) {
                okayButtonText {
                    assertTextEquals(getResourceString(R.string.fragment_disclaimer_button_okay))
                }

                okayButton {
                    assertIsEnabled()
                    performClick()
                }
            }
        }

        step("Enter mock server address and click proceed") {
            onComposeScreen<BaseUrlScreen>(mainActivityRule) {
                progressBar {
                    assertDoesNotExist()
                }
                urlInput {
                    performTextInput(mockWebServer.url("/").toString())
                }
                urlInputLabel {
                    assertTextEquals(getResourceString(R.string.fragment_authentication_input_hint_url))
                }
                proceedButtonText {
                    assertTextEquals(getResourceString(R.string.fragment_base_url_save))
                }
                proceedButton {
                    assertIsEnabled()
                    performClick()
                }
            }
        }

        step("Check that authentication is shown") {
            onComposeScreen<AuthenticationScreen>(mainActivityRule) {
                emailInput {
                    assertIsDisplayed()
                }

                passwordInput {
                    assertIsDisplayed()
                }

                loginButton {
                    assertIsDisplayed()
                }
            }
        }
    }
}