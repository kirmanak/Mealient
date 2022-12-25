package gq.kirmanak.mealient.data.share

import androidx.core.util.PatternsCompat
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
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
        val matcher = PatternsCompat.WEB_URL.matcher(url)
        require(matcher.find()) { "Can't find URL in the text" }
        val urlString = matcher.group()
        val request = ParseRecipeURLInfo(url = urlString, includeTags = true)
        return parseRecipeDataSource.parseRecipeFromURL(request)
    }
}