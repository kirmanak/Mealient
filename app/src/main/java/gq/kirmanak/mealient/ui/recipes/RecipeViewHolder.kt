package gq.kirmanak.mealient.ui.recipes

import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.ViewHolderRecipeBinding
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import timber.log.Timber

class RecipeViewHolder(
    private val binding: ViewHolderRecipeBinding,
    private val recipeImageLoader: RecipeImageLoader,
    private val clickListener: (RecipeSummaryEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private val loadingPlaceholder by lazy {
        binding.root.resources.getString(R.string.view_holder_recipe_text_placeholder)
    }

    fun bind(item: RecipeSummaryEntity?) {
        Timber.v("bind() called with: item = $item")
        binding.name.text = item?.name ?: loadingPlaceholder
        recipeImageLoader.loadRecipeImage(binding.image, item)
        item?.let { entity ->
            binding.root.setOnClickListener {
                Timber.d("bind: item clicked $entity")
                clickListener(entity)
            }
        }
    }
}