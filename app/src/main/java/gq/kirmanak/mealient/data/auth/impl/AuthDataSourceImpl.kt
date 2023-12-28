package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.CreateApiTokenRequest
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val dataSource: MealieDataSource,
) : AuthDataSource {

    override suspend fun authenticate(username: String, password: String): String {
        return dataSource.authenticate(username, password)
    }

    override suspend fun createApiToken(loginToken: String, name: String): String {
        return dataSource.createApiToken(loginToken, CreateApiTokenRequest(name)).token
    }
}