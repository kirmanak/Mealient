package gq.kirmanak.mealient.screen

import com.kaspersky.kaspresso.screens.KScreen
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.activity.MainActivity
import io.github.kakaocup.kakao.text.KButton

object DisclaimerScreen : KScreen<DisclaimerScreen>() {
    override val layoutId = R.layout.fragment_disclaimer
    override val viewClass = MainActivity::class.java

    val okayButton = KButton { withId(R.id.okay) }
}