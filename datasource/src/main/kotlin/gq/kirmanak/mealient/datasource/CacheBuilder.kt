package gq.kirmanak.mealient.datasource

import okhttp3.Cache

interface CacheBuilder {

    fun buildCache(): Cache
}