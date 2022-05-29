package gq.kirmanak.mealient.data.baseurl.impl

import retrofit2.http.GET

interface VersionService {
    @GET("api/debug/version")
    suspend fun getVersion(): VersionResponse
}