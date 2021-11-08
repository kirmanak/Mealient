package gq.kirmanak.mealie.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import gq.kirmanak.mealie.databinding.ViewHolderRecipeBinding
import timber.log.Timber

class RecipesPagingAdapter(
    private val viewModel: RecipeViewModel
) : PagingDataAdapter<RecipeEntity, RecipeViewHolder>(RecipeDiffCallback) {
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
}

class RecipeViewHolder(
    private val binding: ViewHolderRecipeBinding,
    private val recipeViewModel: RecipeViewModel
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RecipeEntity?) {
        binding.name.text = item?.name
        recipeViewModel.loadRecipeImage(binding.image, item)
    }
}

private object RecipeDiffCallback : DiffUtil.ItemCallback<RecipeEntity>() {
    override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem.localId == newItem.localId
    }

    override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem == newItem
    }
}