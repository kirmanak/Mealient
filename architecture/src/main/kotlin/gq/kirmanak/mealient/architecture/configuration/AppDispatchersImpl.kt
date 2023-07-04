package gq.kirmanak.mealient.architecture.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AppDispatchersImpl @Inject constructor() : AppDispatchers {

    override val io: CoroutineDispatcher get() = Dispatchers.IO

    override val main: CoroutineDispatcher get() = Dispatchers.Main

    override val default: CoroutineDispatcher get() = Dispatchers.Default

    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}