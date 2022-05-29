package gq.kirmanak.mealient.data.recipes.impl

import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeImageUrlProviderImpl @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
) : RecipeImageUrlProvider {

    override suspend fun generateImageUrl(slug: String?): String? {
        Timber.v("generateImageUrl() called with: slug = $slug")
        slug?.takeUnless { it.isBlank() } ?: return null
        val imagePath = IMAGE_PATH_FORMAT.format(slug)
        val baseUrl = baseURLStorage.getBaseURL()?.takeUnless { it.isEmpty() }
        val result = baseUrl?.toHttpUrlOrNull()
            ?.newBuilder()
            ?.addPathSegments(imagePath)
            ?.build()
            ?.toString()
        Timber.v("getRecipeImageUrl() returned: $result")
        return result
    }

    companion object {
        private const val IMAGE_PATH_FORMAT = "api/media/recipes/%s/images/original.webp"
    }
}