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

        val urlStart = url.indexOf("http")
        if (urlStart == -1) throw IllegalArgumentException("URL doesn't start with http")

        val startsWithUrl = url.subSequence(urlStart, url.length)
        val urlEnd = startsWithUrl.indexOfFirst { it.isWhitespace() }
            .takeUnless { it == -1 }
            ?: startsWithUrl.length

        val urlString = startsWithUrl.substring(0, urlEnd)
        val request = ParseRecipeURLInfo(url = urlString, includeTags = true)
        return parseRecipeDataSource.parseRecipeFromURL(request)
    }
}