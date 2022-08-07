package gq.kirmanak.mealient.datasource

import okhttp3.OkHttpClient

interface OkHttpBuilder {

    fun buildOkHttp(): OkHttpClient
}