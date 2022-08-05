package gq.kirmanak.mealient.logging

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LoggingModule {

    @Binds
    @Singleton
    fun bindLogger(loggerImpl: LoggerImpl): Logger

    @Binds
    @Singleton
    @IntoSet
    fun bindLogcatAppender(logcatAppender: LogcatAppender): Appender
}