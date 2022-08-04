package gq.kirmanak.mealient.ui.recipes.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.databinding.ViewHolderIngredientBinding
import gq.kirmanak.mealient.ui.recipes.info.RecipeIngredientsAdapter.RecipeIngredientViewHolder
import timber.log.Timber

class RecipeIngredientsAdapter :
    ListAdapter<RecipeIngredientEntity, RecipeIngredientViewHolder>(RecipeIngredientDiffCallback) {

    class RecipeIngredientViewHolder(
        private val binding: ViewHolderIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeIngredientEntity) {
            Timber.v("bind() called with: item = $item")
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
        Timber.v("onCreateViewHolder() called with: parent = $parent, viewType = $viewType")
        val inflater = LayoutInflater.from(parent.context)
        return RecipeIngredientViewHolder(
            ViewHolderIngredientBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeIngredientViewHolder, position: Int) {
        Timber.v("onBindViewHolder() called with: holder = $holder, position = $position")
        val item = getItem(position)
        Timber.d("onBindViewHolder: item is $item")
        holder.bind(item)
    }
}