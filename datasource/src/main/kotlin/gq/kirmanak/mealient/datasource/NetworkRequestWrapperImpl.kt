package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.logging.Logger
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRequestWrapperImpl @Inject constructor(
    private val logger: Logger,
) : NetworkRequestWrapper {

    override suspend fun <T> makeCall(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: () -> String,
    ): Result<T> {
        logger.v { "${logMethod()} called with: ${logParameters()}" }
        return runCatchingExceptCancel { block() }
            .onFailure { logger.e(it) { "${logMethod()} request failed with: ${logParameters()}" } }
            .onSuccess { logger.d { "${logMethod()} request succeeded with ${logParameters()}, result = $it" } }
    }

    override suspend fun <T> makeCallAndHandleUnauthorized(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: () -> String
    ): T = makeCall(block, logMethod, logParameters).getOrElse {
        throw if (it is HttpException && it.code() in listOf(401, 403)) {
            NetworkError.Unauthorized(it)
        } else {
            it
        }
    }

}