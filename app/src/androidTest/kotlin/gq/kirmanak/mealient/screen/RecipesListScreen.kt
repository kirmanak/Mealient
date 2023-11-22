package gq.kirmanak.mealient.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.rules.TestRule

class RecipesListScreen<R : TestRule, A : ComponentActivity>(
    semanticsProvider: AndroidComposeTestRule<R, A>,
) : ComposeScreen<RecipesListScreen<R, A>>(semanticsProvider) {

    init {
        semanticsProvider.onRoot(useUnmergedTree = true).printToLog("RecipesListScreen")
    }

    val errorText: KNode = child { hasTestTag("empty-list-error-text") }
}