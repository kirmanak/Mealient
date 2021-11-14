package gq.kirmanak.mealie.ui.recipes

import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import gq.kirmanak.mealie.databinding.ViewHolderRecipeBinding

class RecipeViewHolder(
    private val binding: ViewHolderRecipeBinding,
    private val recipeViewModel: RecipeViewModel
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RecipeEntity?) {
        binding.name.text = item?.name
        recipeViewModel.loadRecipeImage(binding.image, item)
    }
}