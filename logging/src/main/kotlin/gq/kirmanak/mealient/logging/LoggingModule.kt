package gq.kirmanak.mealient.logging

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface LoggingModule {

    @Binds
    fun bindLogger(loggerImpl: LoggerImpl): Logger

    @Binds
    @IntoSet
    fun bindLogcatAppender(logcatAppender: LogcatAppender): Appender
}