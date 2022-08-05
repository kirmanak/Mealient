package gq.kirmanak.mealient.ui.recipes.images

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@FragmentScoped
class RecipeImageLoaderImpl @Inject constructor(
    private val fragment: Fragment,
    private val requestOptions: RequestOptions,
    private val logger: Logger,
) : RecipeImageLoader {

    override fun loadRecipeImage(view: ImageView, recipe: RecipeSummaryEntity?) {
        logger.v { "loadRecipeImage() called with: view = $view, recipe = $recipe" }
        Glide.with(fragment).load(recipe).apply(requestOptions).into(view)
    }
}