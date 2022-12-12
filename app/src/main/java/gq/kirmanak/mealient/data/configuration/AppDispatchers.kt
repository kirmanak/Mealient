package gq.kirmanak.mealient.data.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface AppDispatchers {
    val io: CoroutineDispatcher
    val main: MainCoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}