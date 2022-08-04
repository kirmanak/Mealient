package gq.kirmanak.mealient.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import timber.log.Timber

class RecipesPagingAdapter(
    private val recipeImageLoader: RecipeImageLoader,
    private val clickListener: (RecipeSummaryEntity) -> Unit
) : PagingDataAdapter<RecipeSummaryEntity, RecipeViewHolder>(RecipeDiffCallback) {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Timber.v("onCreateViewHolder() called with: parent = $parent, viewType = $viewType")
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, recipeImageLoader, clickListener)
    }

    private object RecipeDiffCallback : DiffUtil.ItemCallback<RecipeSummaryEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity
        ): Boolean = oldItem.remoteId == newItem.remoteId

        override fun areContentsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity
        ): Boolean = oldItem.name == newItem.name && oldItem.slug == newItem.slug
    }
}
