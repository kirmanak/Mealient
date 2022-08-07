package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieDataSource: MealieDataSource,
) : AuthDataSource {

    override suspend fun authenticate(username: String, password: String, baseUrl: String): String =
        mealieDataSource.authenticate(baseUrl, username, password)
}