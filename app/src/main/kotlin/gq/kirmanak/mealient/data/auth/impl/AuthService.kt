package gq.kirmanak.mealient.data.auth.impl

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("/api/auth/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<GetTokenResponse>
}