package gq.kirmanak.mealient.datasource

interface NetworkRequestWrapper {

    suspend fun <T> makeCall(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: () -> String,
    ): Result<T>

    suspend fun <T> makeCallAndHandleUnauthorized(
        block: suspend () -> T,
        logMethod: () -> String,
        logParameters: () -> String,
    ): T

}