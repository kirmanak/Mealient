package gq.kirmanak.mealient.logging

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LoggerModule {

    @Binds
    fun bindLogger(loggerImpl: LoggerImpl): Logger

}