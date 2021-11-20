package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.databinding.ViewHolderInstructionBinding

class RecipeInstructionsAdapter :
    ListAdapter<RecipeInstructionEntity, RecipeInstructionsAdapter.RecipeInstructionViewHolder>(
        RecipeInstructionDiffCallback
    ) {

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

    class RecipeInstructionViewHolder(private val binding: ViewHolderInstructionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeInstructionEntity, position: Int) {
            binding.step.text = "Step: ${position + 1}"
            binding.instruction.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeInstructionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeInstructionViewHolder(
            ViewHolderInstructionBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeInstructionViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}