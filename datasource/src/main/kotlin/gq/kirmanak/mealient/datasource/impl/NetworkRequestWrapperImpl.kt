package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import retrofit2.HttpException
import javax.inject.Inject

internal class NetworkRequestWrapperImpl @Inject constructor(
    private val logger: Logger,
) : NetworkRequestWrapper {

    override suspend fun <T> makeCall(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: (() -> String)?,
    ): Result<T> {
        logger.v {
            if (logParameters == null) {
                "${logMethod()} called"
            } else {
                "${logMethod()} called with: ${logParameters()}"
            }
        }
        return runCatchingExceptCancel { block() }
            .onFailure {
                logger.e(it) {
                    if (logParameters == null) {
                        "${logMethod()} request failed"
                    } else {
                        "${logMethod()} request failed with: ${logParameters()}"
                    }
                }
            }
            .onSuccess {
                logger.d {
                    if (logParameters == null) {
                        "${logMethod()} request succeeded, result = $it"
                    } else {
                        "${logMethod()} request succeeded with: ${logParameters()}, result = $it"
                    }
                }
            }
    }

    override suspend fun <T> makeCallAndHandleUnauthorized(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: (() -> String)?
    ): T = makeCall(block, logMethod, logParameters).getOrElse {
        throw if (it is HttpException && it.code() in listOf(401, 403)) {
            NetworkError.Unauthorized(it)
        } else {
            it
        }
    }

}