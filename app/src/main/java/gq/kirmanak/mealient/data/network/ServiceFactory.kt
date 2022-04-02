package gq.kirmanak.mealient.data.network

interface ServiceFactory<T> {

    fun provideService(baseUrl: String): T
}