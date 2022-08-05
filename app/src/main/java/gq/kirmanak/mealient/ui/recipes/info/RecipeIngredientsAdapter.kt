package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.databinding.ViewHolderIngredientBinding
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.info.RecipeIngredientsAdapter.RecipeIngredientViewHolder
import javax.inject.Inject
import javax.inject.Singleton

class RecipeIngredientsAdapter private constructor(
    private val recipeIngredientViewHolderFactory: RecipeIngredientViewHolder.Factory,
    private val logger: Logger,
) : ListAdapter<RecipeIngredientEntity, RecipeIngredientViewHolder>(RecipeIngredientDiffCallback) {

    @Singleton
    class Factory @Inject constructor(
        private val recipeIngredientViewHolderFactory: RecipeIngredientViewHolder.Factory,
        private val logger: Logger,
    ) {
        fun build() = RecipeIngredientsAdapter(recipeIngredientViewHolderFactory, logger)
    }

    class RecipeIngredientViewHolder private constructor(
        private val binding: ViewHolderIngredientBinding,
        private val logger: Logger,
    ) : RecyclerView.ViewHolder(binding.root) {

        @Singleton
        class Factory @Inject constructor(
            private val logger: Logger,
        ) {

            fun build(binding: ViewHolderIngredientBinding) =
                RecipeIngredientViewHolder(binding, logger)
        }

        fun bind(item: RecipeIngredientEntity) {
            logger.v { "bind() called with: item = $item" }
            binding.checkBox.text = item.note
        }
    }

    private object RecipeIngredientDiffCallback : DiffUtil.ItemCallback<RecipeIngredientEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeIngredientEntity,
            newItem: RecipeIngredientEntity
        ): Boolean = oldItem.localId == newItem.localId

        override fun areContentsTheSame(
            oldItem: RecipeIngredientEntity,
            newItem: RecipeIngredientEntity
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientViewHolder {
        logger.v { "onCreateViewHolder() called with: parent = $parent, viewType = $viewType" }
        val inflater = LayoutInflater.from(parent.context)
        return recipeIngredientViewHolderFactory.build(
            ViewHolderIngredientBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeIngredientViewHolder, position: Int) {
        logger.v { "onBindViewHolder() called with: holder = $holder, position = $position" }
        val item = getItem(position)
        logger.d { "onBindViewHolder: item is $item" }
        holder.bind(item)
    }
}