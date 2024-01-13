package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

internal class RecipesListScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<RecipesListScreen>(semanticsProvider) {

    val openDrawerButton = child<KNode> { hasTestTag("open-drawer-button") }

    val searchRecipesField = child<KNode> { hasTestTag("search-recipes-field") }

    val emptyListErrorText = unmergedChild<KNode, RecipesListScreen> {
        hasTestTag("empty-list-error-text")
    }
}