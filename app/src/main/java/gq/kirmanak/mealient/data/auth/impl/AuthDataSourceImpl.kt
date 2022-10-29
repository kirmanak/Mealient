package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AuthDataSource {

    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String,
        serverVersion: ServerVersion,
    ): String = when (serverVersion) {
        ServerVersion.V0 -> v0Source.authenticate(baseUrl, username, password)
        ServerVersion.V1 -> v1Source.authenticate(baseUrl, username, password)
    }
}