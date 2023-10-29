package gq.kirmanak.mealient

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.datasource.ktor.KtorConfiguration

@Module
@InstallIn(SingletonComponent::class)
internal interface DebugModule {

    @Binds
    @IntoSet
    fun bindLoggingKtorConfiguration(loggingKtorConfiguration: LoggingKtorConfiguration): KtorConfiguration
}
