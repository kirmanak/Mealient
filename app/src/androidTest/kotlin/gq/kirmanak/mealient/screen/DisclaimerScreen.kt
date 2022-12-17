package gq.kirmanak.mealient.screen

import com.kaspersky.kaspresso.screens.KScreen
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.disclaimer.DisclaimerFragment
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

object DisclaimerScreen : KScreen<DisclaimerScreen>() {
    override val layoutId = R.layout.fragment_disclaimer
    override val viewClass = DisclaimerFragment::class.java

    val okayButton = KButton { withId(R.id.okay) }

    val disclaimerText = KTextView { withId(R.id.main_text) }
}