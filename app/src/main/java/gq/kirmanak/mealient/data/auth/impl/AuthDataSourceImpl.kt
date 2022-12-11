package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v0.models.CreateApiTokenRequestV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenRequestV1
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AuthDataSource {

    private suspend fun getVersion(): ServerVersion = serverInfoRepo.getVersion()

    override suspend fun authenticate(
        username: String,
        password: String,
    ): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.authenticate(username, password)
        ServerVersion.V1 -> v1Source.authenticate(username, password)
    }

    override suspend fun createApiToken(name: String): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.createApiToken(CreateApiTokenRequestV0(name))
        ServerVersion.V1 -> v1Source.createApiToken(CreateApiTokenRequestV1(name)).token
    }
}