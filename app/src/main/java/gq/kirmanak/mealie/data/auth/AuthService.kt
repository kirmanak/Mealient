package gq.kirmanak.mealie.data.auth

import gq.kirmanak.mealie.data.auth.impl.GetTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("/api/auth/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String? = null,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null
    ): GetTokenResponse
}