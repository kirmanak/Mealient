package gq.kirmanak.mealient.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import javax.inject.Inject
import javax.inject.Singleton

class RecipesPagingAdapter private constructor(
    private val logger: Logger,
    private val recipeImageLoader: RecipeImageLoader,
    private val recipeViewHolderFactory: RecipeViewHolder.Factory,
    private val clickListener: (RecipeSummaryEntity) -> Unit
) : PagingDataAdapter<RecipeSummaryEntity, RecipeViewHolder>(RecipeDiffCallback) {

    @Singleton
    class Factory @Inject constructor(
        private val logger: Logger,
        private val recipeViewHolderFactory: RecipeViewHolder.Factory,
    ) {

        fun build(
            recipeImageLoader: RecipeImageLoader,
            clickListener: (RecipeSummaryEntity) -> Unit,
        ) = RecipesPagingAdapter(logger, recipeImageLoader, recipeViewHolderFactory, clickListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        logger.v { "onCreateViewHolder() called with: parent = $parent, viewType = $viewType" }
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderRecipeBinding.inflate(inflater, parent, false)
        return recipeViewHolderFactory.build(recipeImageLoader, binding, clickListener)
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
