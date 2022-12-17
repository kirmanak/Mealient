package gq.kirmanak.mealient.screen

import com.kaspersky.kaspresso.screens.KScreen
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.recipes.RecipesListFragment
import io.github.kakaocup.kakao.text.KTextView

object RecipesListScreen : KScreen<RecipesListScreen>() {
    override val layoutId: Int = R.layout.fragment_recipes_list
    override val viewClass: Class<*> = RecipesListFragment::class.java

    val emptyListText = KTextView { withId(R.id.empty_list_text) }
}