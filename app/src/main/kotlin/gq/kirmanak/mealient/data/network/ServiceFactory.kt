package gq.kirmanak.mealient.data.network

interface ServiceFactory<T> {

    suspend fun provideService(baseUrl: String? = null): T
}