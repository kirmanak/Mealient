package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.databinding.ViewHolderIngredientBinding
import gq.kirmanak.mealient.ui.recipes.info.RecipeIngredientsAdapter.RecipeIngredientViewHolder

class RecipeIngredientsAdapter() :
    ListAdapter<RecipeIngredientEntity, RecipeIngredientViewHolder>(RecipeIngredientDiffCallback) {

    class RecipeIngredientViewHolder(
        private val binding: ViewHolderIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeIngredientEntity) {
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
        val inflater = LayoutInflater.from(parent.context)
        return RecipeIngredientViewHolder(
            ViewHolderIngredientBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeIngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}