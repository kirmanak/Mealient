package gq.kirmanak.mealient.ui.recipes

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.extensions.resources
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader

class RecipeViewHolder @AssistedInject constructor(
    private val logger: Logger,
    @Assisted private val binding: ViewHolderRecipeBinding,
    private val recipeImageLoader: RecipeImageLoader,
    @Assisted private val showFavoriteIcon: Boolean,
    @Assisted private val clickListener: (ClickEvent) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    @FragmentScoped
    @AssistedFactory
    interface Factory {

        fun build(
            showFavoriteIcon: Boolean,
            binding: ViewHolderRecipeBinding,
            clickListener: (ClickEvent) -> Unit,
        ): RecipeViewHolder

    }

    sealed class ClickEvent {

        abstract val recipeSummaryEntity: RecipeSummaryEntity

        data class FavoriteClick(
            override val recipeSummaryEntity: RecipeSummaryEntity
        ) : ClickEvent()

        data class RecipeClick(
            override val recipeSummaryEntity: RecipeSummaryEntity
        ) : ClickEvent()

        data class DeleteClick(
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

            binding.favoriteIcon.isVisible = showFavoriteIcon
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

            binding.deleteIcon.setOnClickListener {
                clickListener(ClickEvent.DeleteClick(item))
            }
        }
    }
}

private fun View.setContentDescription(@StringRes resId: Int) {
    contentDescription = context.getString(resId)
}