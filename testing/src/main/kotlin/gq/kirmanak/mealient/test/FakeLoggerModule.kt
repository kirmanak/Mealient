package gq.kirmanak.mealient.test

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.logging.LoggerModule

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LoggerModule::class]
)
interface FakeLoggerModule {

    @Binds
    fun bindFakeLogger(impl: FakeLogger): Logger
}