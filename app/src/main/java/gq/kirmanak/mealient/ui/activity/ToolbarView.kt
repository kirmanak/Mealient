package gq.kirmanak.mealient.ui.activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import gq.kirmanak.mealient.databinding.ViewToolbarBinding

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

    fun onSearchQueryChanged(block: (String) -> Unit) {
        binding.searchEdit.doAfterTextChanged { block(it.toString()) }
    }
}