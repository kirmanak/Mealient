package gq.kirmanak.mealient.ui.util

import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

// @AssistedFactory does not currently support type parameters in the creator method.
// See https://github.com/google/dagger/issues/2279
internal class LoadingHelperFactoryImpl @Inject constructor(
    private val logger: Logger
) : LoadingHelperFactory {

    override fun <T> create(
        coroutineScope: CoroutineScope,
        fetch: suspend () -> Result<T>,
    ): LoadingHelper<T> = LoadingHelperImpl(logger, fetch)
}