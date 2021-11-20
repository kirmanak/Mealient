package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.databinding.ViewHolderInstructionBinding
import gq.kirmanak.mealient.ui.recipes.info.RecipeInstructionsAdapter.RecipeInstructionViewHolder
import timber.log.Timber

class RecipeInstructionsAdapter :
    ListAdapter<RecipeInstructionEntity, RecipeInstructionViewHolder>(RecipeInstructionDiffCallback) {

    private object RecipeInstructionDiffCallback :
        DiffUtil.ItemCallback<RecipeInstructionEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeInstructionEntity,
            newItem: RecipeInstructionEntity
        ): Boolean = oldItem.localId == newItem.localId

        override fun areContentsTheSame(
            oldItem: RecipeInstructionEntity,
            newItem: RecipeInstructionEntity
        ): Boolean = oldItem == newItem
    }

    class RecipeInstructionViewHolder(
        private val binding: ViewHolderInstructionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeInstructionEntity, position: Int) {
            Timber.v("bind() called with: item = $item, position = $position")
            binding.step.text = binding.root.resources.getString(
                R.string.view_holder_recipe_instructions_step, position + 1
            )
            binding.instruction.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeInstructionViewHolder {
        Timber.v("onCreateViewHolder() called with: parent = $parent, viewType = $viewType")
        val inflater = LayoutInflater.from(parent.context)
        return RecipeInstructionViewHolder(
            ViewHolderInstructionBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeInstructionViewHolder, position: Int) {
        Timber.v("onBindViewHolder() called with: holder = $holder, position = $position")
        val item = getItem(position)
        Timber.d("onBindViewHolder: item is $item")
        holder.bind(item, position)
    }
}