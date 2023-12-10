package gq.kirmanak.mealient.logging

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface AppenderModule {

    @Binds
    @IntoSet
    fun bindLogcatAppender(logcatAppender: LogcatAppender): Appender

    @Binds
    @IntoSet
    fun bindFileAppender(fileAppender: FileAppender): Appender
}