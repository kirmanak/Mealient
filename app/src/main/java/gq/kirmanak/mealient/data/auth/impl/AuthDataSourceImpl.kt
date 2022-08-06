package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.extensions.logAndMapErrors
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieDataSource: MealieDataSource,
) : AuthDataSource {

    override suspend fun authenticate(username: String, password: String, baseUrl: String): String {
        logger.v { "authenticate() called with: username = $username, password = $password" }
        val accessToken = logger.logAndMapErrors(
            block = { mealieDataSource.authenticate(baseUrl, username, password) },
            logProvider = { "sendRequest: can't get token" },
        )
        logger.v { "authenticate() returned: $accessToken" }
        return accessToken
    }
}