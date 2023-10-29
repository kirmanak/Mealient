package gq.kirmanak.mealient.datasource.ktor

import io.ktor.client.HttpClient

internal interface KtorClientBuilder {

    fun buildKtorClient(): HttpClient
}