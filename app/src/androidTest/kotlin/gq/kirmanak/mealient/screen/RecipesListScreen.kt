package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class RecipesListScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<RecipesListScreen>(semanticsProvider) {

    init {
        semanticsProvider
            .onRoot(useUnmergedTree = true)
            .printToLog("DisclaimerScreen")
    }

    val errorText: KNode = child { hasTestTag("empty-list-error-text") }
}