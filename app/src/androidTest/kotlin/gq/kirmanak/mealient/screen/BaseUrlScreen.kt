package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class BaseUrlScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<BaseUrlScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag("base-url-screen") },
) {

    val urlInput = child<KNode> { hasTestTag("url-input-field") }

    val urlInputLabel = unmergedChild<KNode, BaseUrlScreen> { hasTestTag("url-input-label") }

    val proceedButton = child<KNode> { hasTestTag("proceed-button") }

    val proceedButtonText =
        unmergedChild<KNode, BaseUrlScreen> { hasTestTag("proceed-button-text") }

    val progressBar = unmergedChild<KNode, BaseUrlScreen> { hasTestTag("progress-indicator") }

}