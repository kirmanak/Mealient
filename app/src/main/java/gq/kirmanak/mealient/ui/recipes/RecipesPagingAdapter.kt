package gq.kirmanak.mealient.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.logging.Logger

class RecipesPagingAdapter @AssistedInject constructor(
    private val logger: Logger,
    private val recipeViewHolderFactory: RecipeViewHolder.Factory,
    @Assisted private val showFavoriteIcon: Boolean,
    @Assisted private val clickListener: (RecipeViewHolder.ClickEvent) -> Unit
) : PagingDataAdapter<RecipeSummaryEntity, RecipeViewHolder>(RecipeDiffCallback) {

    @FragmentScoped
    @AssistedFactory
    interface Factory {

        fun build(
            showFavoriteIcon: Boolean,
            clickListener: (RecipeViewHolder.ClickEvent) -> Unit,
        ): RecipesPagingAdapter
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        logger.d { "onBindViewHolder() called with: holder = $holder, position = $position" }
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        logger.v { "onCreateViewHolder() called with: parent = $parent, viewType = $viewType" }
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderRecipeBinding.inflate(inflater, parent, false)
        return recipeViewHolderFactory.build(showFavoriteIcon, binding, clickListener)
    }

    private object RecipeDiffCallback : DiffUtil.ItemCallback<RecipeSummaryEntity>() {
        override fun areItemsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity,
        ): Boolean = oldItem.remoteId == newItem.remoteId

        override fun areContentsTheSame(
            oldItem: RecipeSummaryEntity,
            newItem: RecipeSummaryEntity,
        ): Boolean = oldItem == newItem
    }
}
