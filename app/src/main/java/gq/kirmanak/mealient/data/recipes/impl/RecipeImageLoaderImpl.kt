package gq.kirmanak.mealient.data.recipes.impl

import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.extensions.launchWhenViewResumed
import gq.kirmanak.mealient.ui.images.ImageLoader
import gq.kirmanak.mealient.ui.recipes.RecipeImageLoader
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class RecipeImageLoaderImpl @Inject constructor(
    private val imageLoader: ImageLoader,
    private val baseURLStorage: BaseURLStorage,
    private val fragment: Fragment,
): RecipeImageLoader {

    override fun loadRecipeImage(view: ImageView, slug: String?) {
        Timber.v("loadRecipeImage() called with: view = $view, slug = $slug")
        fragment.launchWhenViewResumed {
            imageLoader.loadImage(generateImageUrl(slug), R.drawable.placeholder_recipe, view)
        }
    }

    @VisibleForTesting
    suspend fun generateImageUrl(slug: String?): String? {
        Timber.v("generateImageUrl() called with: slug = $slug")
        val result = baseURLStorage.getBaseURL()
            ?.takeIf { it.isNotBlank() }
            ?.takeUnless { slug.isNullOrBlank() }
            ?.toHttpUrlOrNull()
            ?.newBuilder()
            ?.addPathSegments("api/media/recipes/$slug/images/original.webp")
            ?.build()
            ?.toString()
        Timber.v("generateImageUrl() returned: $result")
        return result
    }
}