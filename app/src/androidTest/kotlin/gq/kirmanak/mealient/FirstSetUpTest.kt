package gq.kirmanak.mealient

import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.screen.BaseUrlScreen
import gq.kirmanak.mealient.screen.DisclaimerScreen
import gq.kirmanak.mealient.screen.RecipesListScreen
import org.junit.Test

@HiltAndroidTest
class FirstSetUpTest : BaseTestCase() {

    override fun setUp() {
        super.setUp()
        mockWebServer.dispatch { url, _ ->
            if (url == "/api/app/about") versionV1Response else notFoundResponse
        }
    }

    @Test
    fun test() = run {
        step("Ensure button is disabled") {
            DisclaimerScreen {
                okayButton {
                    isVisible()
                    isDisabled()
                    hasAnyText()
                }

                disclaimerText {
                    isVisible()
                    hasText(R.string.fragment_disclaimer_main_text)
                }
            }
        }

        step("Close disclaimer screen") {
            DisclaimerScreen {
                okayButton {
                    isVisible()
                    isEnabled()
                    hasText(R.string.fragment_disclaimer_button_okay)
                    click()
                }
            }
        }

        step("Enter mock server address and click proceed") {
            BaseUrlScreen {
                progressBar {
                    isGone()
                }
                urlInput {
                    isVisible()
                    edit.replaceText(mockWebServer.url("/").toString())
                    hasHint(R.string.fragment_authentication_input_hint_url)
                }
                proceedButton {
                    isVisible()
                    isEnabled()
                    hasText(R.string.fragment_base_url_save)
                    click()
                }
            }
        }

        step("Check that empty list of recipes is shown") {
            RecipesListScreen {
                emptyListText {
                    isVisible()
                    hasText(R.string.fragment_recipes_list_no_recipes)
                }
            }
        }
    }
}