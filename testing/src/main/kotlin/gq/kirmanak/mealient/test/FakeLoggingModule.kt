package gq.kirmanak.mealient.test

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.logging.LoggingModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LoggingModule::class]
)
interface FakeLoggingModule {

    @Binds
    @Singleton
    fun bindFakeLogger(impl: FakeLogger): Logger
}