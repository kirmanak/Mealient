package gq.kirmanak.mealient.datasource.impl

import android.content.Context
import android.os.StatFs
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.logging.Logger
import okhttp3.Cache
import java.io.File
import javax.inject.Inject

internal class CacheBuilderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
) {

    fun buildCache(): Cache {
        val dir = findCacheDir()
        return Cache(dir, calculateDiskCacheSize(dir))
    }

    private fun findCacheDir(): File = File(context.cacheDir, "okhttp")

    private fun calculateDiskCacheSize(dir: File): Long = dir.runCatching {
        StatFs(absolutePath).let {
            it.blockCountLong * it.blockSizeLong * AVAILABLE_SPACE_PERCENT / 100
        }
    }
        .onFailure { logger.e(it) { "Can't get available space" } }
        .getOrNull()
        ?.coerceAtLeast(MIN_OKHTTP_CACHE_SIZE)
        ?.coerceAtMost(MAX_OKHTTP_CACHE_SIZE)
        ?: MIN_OKHTTP_CACHE_SIZE

    companion object {
        private const val MIN_OKHTTP_CACHE_SIZE = 5 * 1024 * 1024L // 5MB
        private const val MAX_OKHTTP_CACHE_SIZE = 50 * 1024 * 1024L // 50MB
        private const val AVAILABLE_SPACE_PERCENT = 2
    }
}