package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val v0Source: MealieDataSourceV0,
) : AuthDataSource {

    override suspend fun authenticate(username: String, password: String, baseUrl: String): String =
        v0Source.authenticate(baseUrl, username, password)
}