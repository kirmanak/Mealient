package gq.kirmanak.mealient.screen

import io.github.kakaocup.compose.node.builder.ViewBuilder
import io.github.kakaocup.compose.node.core.BaseNode


inline fun <reified N, T : BaseNode<T>> BaseNode<T>.unmergedChild(
    function: ViewBuilder.() -> Unit,
): N = child<N> {
    useUnmergedTree = true
    function()
}