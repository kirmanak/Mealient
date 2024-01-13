package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class DisclaimerScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<DisclaimerScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag("disclaimer-screen") },
) {

    val okayButton = child<KNode> { hasTestTag("okay-button") }

    val okayButtonText = unmergedChild<KNode, DisclaimerScreen> { hasTestTag("okay-button-text") }

    val disclaimerText = child<KNode> { hasTestTag("disclaimer-text") }
}