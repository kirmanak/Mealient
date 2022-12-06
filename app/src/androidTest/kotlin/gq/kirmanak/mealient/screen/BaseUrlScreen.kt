package gq.kirmanak.mealient.screen

import com.kaspersky.kaspresso.screens.KScreen
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.baseurl.BaseURLFragment
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.text.KButton

object BaseUrlScreen : KScreen<BaseUrlScreen>() {
    override val layoutId = R.layout.fragment_base_url
    override val viewClass = BaseURLFragment::class.java

    val urlInput = KTextInputLayout { withId(R.id.url_input_layout) }

    val proceedButton = KButton { withId(R.id.button) }

    val progressBar = KProgressBar { withId(R.id.progress)}
}