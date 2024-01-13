package gq.kirmanak.mealient.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AuthenticationScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<AuthenticationScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag("authentication-screen") },
) {

    val emailInput = child<KNode> { hasTestTag("email-input") }

    val passwordInput = child<KNode> { hasTestTag("password-input") }

    val loginButton = child<KNode> { hasTestTag("login-button") }

}