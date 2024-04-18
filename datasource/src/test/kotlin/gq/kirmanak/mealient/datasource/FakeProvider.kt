package gq.kirmanak.mealient.datasource

import javax.inject.Provider

data class FakeProvider<T>(
    val value: T,
) : Provider<T> {

    override fun get(): T = value
}
