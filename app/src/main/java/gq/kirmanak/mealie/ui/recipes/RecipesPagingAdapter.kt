package gq.kirmanak.mealie.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import gq.kirmanak.mealie.data.recipes.db.RecipeSummaryEntity
import gq.kirmanak.mealie.databinding.ViewHolderRecipeBinding
import timber.log.Timber

class RecipesPagingAdapter(
    private val viewModel: RecipeViewModel
) : PagingDataAdapter<RecipeSummaryEntity, RecipeViewHolder>(RecipeDiffCallback) {
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Timber.v("onCreateViewHolder() called with: parent = $parent, viewType = $viewType")
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, viewModel)
    }

    private object RecipeDiffCallback : DiffUtil.ItemCallback<RecipeSummaryEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity
        ): Boolean {
            return oldItem.remoteId == newItem.remoteId
        }

        override fun areContentsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.slug == newItem.slug
        }
    }
}
