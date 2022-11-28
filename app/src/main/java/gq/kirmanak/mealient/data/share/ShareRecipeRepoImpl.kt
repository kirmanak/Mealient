package gq.kirmanak.mealient.data.share

import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareRecipeRepoImpl @Inject constructor(
    private val logger: Logger,
    private val parseRecipeDataSource: ParseRecipeDataSource,
) : ShareRecipeRepo {

    override suspend fun saveRecipeByURL(url: CharSequence): String {
        logger.v { "saveRecipeByURL() called with: url = $url" }
        val request = ParseRecipeURLInfo(url = url.toString(), includeTags = true)
        return parseRecipeDataSource.parseRecipeFromURL(request)
    }
}