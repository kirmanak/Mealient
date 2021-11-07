package gq.kirmanak.mealie.data.auth

import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val storage: AuthStorage
) : AuthRepo {
    override suspend fun isAuthenticated(): Boolean = storage.isAuthenticated()

    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String
    ): Throwable? {
        val authResult = dataSource.authenticate(username, password, baseUrl)
        if (authResult.isFailure) return authResult.exceptionOrNull()
        val token = checkNotNull(authResult.getOrNull())
        storage.storeToken(token)
        return null
    }
}