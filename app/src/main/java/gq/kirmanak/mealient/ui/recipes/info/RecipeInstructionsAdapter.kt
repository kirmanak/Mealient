package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.databinding.ViewHolderInstructionBinding
import gq.kirmanak.mealient.extensions.resources
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.info.RecipeInstructionsAdapter.RecipeInstructionViewHolder
import javax.inject.Inject

class RecipeInstructionsAdapter @Inject constructor(
    private val logger: Logger,
    private val recipeInstructionViewHolderFactory: RecipeInstructionViewHolder.Factory,
) : ListAdapter<RecipeInstructionEntity, RecipeInstructionViewHolder>(RecipeInstructionDiffCallback) {

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

    class RecipeInstructionViewHolder @AssistedInject constructor(
        @Assisted private val binding: ViewHolderInstructionBinding,
        private val logger: Logger,
    ) : RecyclerView.ViewHolder(binding.root) {

        @AssistedFactory
        interface Factory {
            fun build(binding: ViewHolderInstructionBinding): RecipeInstructionViewHolder
        }

        fun bind(item: RecipeInstructionEntity, position: Int) {
            logger.v { "bind() called with: item = $item, position = $position" }
            binding.step.text = binding.resources.getString(
                R.string.view_holder_recipe_instructions_step, position + 1
            )
            binding.instruction.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeInstructionViewHolder {
        logger.v { "onCreateViewHolder() called with: parent = $parent, viewType = $viewType" }
        val inflater = LayoutInflater.from(parent.context)
        return recipeInstructionViewHolderFactory.build(
            ViewHolderInstructionBinding.inflate(inflater, parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecipeInstructionViewHolder, position: Int) {
        logger.v { "onBindViewHolder() called with: holder = $holder, position = $position" }
        val item = getItem(position)
        logger.d { "onBindViewHolder: item is $item" }
        holder.bind(item, position)
    }
}