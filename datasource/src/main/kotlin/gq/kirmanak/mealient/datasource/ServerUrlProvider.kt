package gq.kirmanak.mealient.datasource

interface ServerUrlProvider {

    suspend fun getUrl(): String?
}