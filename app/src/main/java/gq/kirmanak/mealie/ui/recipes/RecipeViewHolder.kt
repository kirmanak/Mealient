package gq.kirmanak.mealie.ui.recipes

import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealie.R
import gq.kirmanak.mealie.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealie.databinding.ViewHolderRecipeBinding

class RecipeViewHolder(
    private val binding: ViewHolderRecipeBinding,
    private val recipeViewModel: RecipeViewModel,
    private val clickListener: (RecipeSummaryEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val loadingPlaceholder by lazy {
        binding.root.resources.getString(R.string.view_holder_recipe_text_placeholder)
    }

    fun bind(item: RecipeSummaryEntity?) {
        binding.name.text = item?.name ?: loadingPlaceholder
        recipeViewModel.loadRecipeImage(binding.image, item)
        item?.let { entity -> binding.root.setOnClickListener { clickListener(entity) } }
    }
}