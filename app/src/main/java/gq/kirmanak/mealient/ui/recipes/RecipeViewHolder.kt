package gq.kirmanak.mealient.ui.recipes

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.extensions.resources
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import javax.inject.Inject

class RecipeViewHolder private constructor(
    private val logger: Logger,
    private val binding: ViewHolderRecipeBinding,
    private val recipeImageLoader: RecipeImageLoader,
    private val clickListener: (ClickEvent) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    @FragmentScoped
    class Factory @Inject constructor(
        private val recipeImageLoader: RecipeImageLoader,
        private val logger: Logger,
    ) {

        fun build(
            binding: ViewHolderRecipeBinding,
            clickListener: (ClickEvent) -> Unit,
        ) = RecipeViewHolder(logger, binding, recipeImageLoader, clickListener)

    }

    sealed class ClickEvent {

        abstract val recipeSummaryEntity: RecipeSummaryEntity

        data class FavoriteClick(
            override val recipeSummaryEntity: RecipeSummaryEntity
        ) : ClickEvent()

        data class RecipeClick(
            override val recipeSummaryEntity: RecipeSummaryEntity
        ) : ClickEvent()

    }

    private val loadingPlaceholder by lazy {
        binding.resources.getString(R.string.view_holder_recipe_text_placeholder)
    }

    fun bind(item: RecipeSummaryEntity?) {
        logger.v { "bind() called with: item = $item" }
        binding.name.text = item?.name ?: loadingPlaceholder
        recipeImageLoader.loadRecipeImage(binding.image, item)
        item?.let { entity ->
            binding.root.setOnClickListener {
                logger.d { "bind: item clicked $entity" }
                clickListener(ClickEvent.RecipeClick(entity))
            }
            binding.favoriteIcon.setOnClickListener {
                clickListener(ClickEvent.FavoriteClick(entity))
            }
            binding.favoriteIcon.setImageResource(
                if (item.isFavorite) {
                    R.drawable.ic_favorite_filled
                } else {
                    R.drawable.ic_favorite_unfilled
                }
            )
            binding.favoriteIcon.setContentDescription(
                if (item.isFavorite) {
                    R.string.view_holder_recipe_favorite_content_description
                } else {
                    R.string.view_holder_recipe_non_favorite_content_description
                }
            )
        }
    }
}

private fun View.setContentDescription(@StringRes resId: Int) {
    contentDescription = context.getString(resId)
}