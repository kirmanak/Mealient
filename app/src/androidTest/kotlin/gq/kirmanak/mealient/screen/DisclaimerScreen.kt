package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class DisclaimerScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<DisclaimerScreen>(semanticsProvider) {

    init {
        semanticsProvider
            .onRoot(useUnmergedTree = true)
            .printToLog("DisclaimerScreen")
    }

    val okayButton = child<KNode> { hasTestTag("okay-button") }

    val okayButtonText = child<KNode> { hasTestTag("okay-button-text") }

    val disclaimerText = child<KNode> { hasTestTag("disclaimer-text") }
}