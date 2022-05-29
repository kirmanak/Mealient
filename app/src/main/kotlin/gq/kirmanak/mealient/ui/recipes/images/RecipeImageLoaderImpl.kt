package gq.kirmanak.mealient.ui.recipes.images

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class RecipeImageLoaderImpl @Inject constructor(
    private val fragment: Fragment,
    private val requestOptions: RequestOptions,
) : RecipeImageLoader {

    override fun loadRecipeImage(view: ImageView, recipe: RecipeSummaryEntity?) {
        Timber.v("loadRecipeImage() called with: view = $view, recipe = $recipe")
        Glide.with(fragment).load(recipe).apply(requestOptions).into(view)
    }
}