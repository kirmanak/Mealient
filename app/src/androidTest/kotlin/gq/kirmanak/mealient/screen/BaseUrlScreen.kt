package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.kakao.progress.KProgressBar

class BaseUrlScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<BaseUrlScreen>(semanticsProvider) {

    val urlInput = child<KNode> { hasTestTag("url-input-field") }

    val urlInputLabel = child<KNode> { hasTestTag("url-input-label") }

    val proceedButton = child<KNode> { hasTestTag("proceed-button") }

    val proceedButtonText = child<KNode> { hasTestTag("proceed-button-text") }

    val progressBar = child<KProgressBar> { hasTestTag("progress-indicator") }
}