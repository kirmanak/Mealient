package gq.kirmanak.mealient.data.recipes.impl

import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.logging.Logger
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

class RecipeImageUrlProviderImpl @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val logger: Logger,
) : RecipeImageUrlProvider {

    override suspend fun generateImageUrl(slug: String?): String? {
        logger.v { "generateImageUrl() called with: slug = $slug" }
        slug?.takeUnless { it.isBlank() } ?: return null
        val imagePath = IMAGE_PATH_FORMAT.format(slug)
        val baseUrl = serverInfoRepo.getUrl()?.takeUnless { it.isEmpty() }
        val result = baseUrl?.toHttpUrlOrNull()
            ?.newBuilder()
            ?.addPathSegments(imagePath)
            ?.build()
            ?.toString()
        logger.v { "getRecipeImageUrl() returned: $result" }
        return result
    }

    companion object {
        private const val IMAGE_PATH_FORMAT = "api/media/recipes/%s/images/original.webp"
    }
}