package gq.kirmanak.mealient.ui.activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gq.kirmanak.mealient.databinding.ViewToolbarBinding
import gq.kirmanak.mealient.extensions.hideKeyboard
import gq.kirmanak.mealient.extensions.textChangesFlow
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private lateinit var binding: ViewToolbarBinding

    var isSearchVisible: Boolean
        get() = binding.searchEdit.isVisible
        set(value) {
            binding.searchEdit.isVisible = value
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val inflater = LayoutInflater.from(context)
        binding = ViewToolbarBinding.inflate(inflater, this)
    }

    fun setNavigationOnClickListener(listener: OnClickListener?) {
        binding.navigationIcon.setOnClickListener(listener)
    }

    fun searchQueriesFlow(logger: Logger): Flow<CharSequence?> {
        return binding.searchEdit.textChangesFlow(logger)
    }

    fun clearSearchFocus() {
        binding.searchEdit.clearFocus()
        hideKeyboard()
    }
}