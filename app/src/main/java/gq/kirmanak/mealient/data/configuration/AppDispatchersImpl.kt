package gq.kirmanak.mealient.data.configuration

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDispatchersImpl @Inject constructor() : AppDispatchers {

    override val io = Dispatchers.IO

    override val main = Dispatchers.Main

    override val default = Dispatchers.Default

    override val unconfined = Dispatchers.Unconfined
}