package gq.kirmanak.mealient.datasource.ktor

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.datasource.SignOutHandler
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface KtorModule {

    companion object {

        @Provides
        @Singleton
        fun provideClient(builder: KtorClientBuilder): HttpClient = builder.buildKtorClient()
    }

    @Binds
    @IntoSet
    fun bindAuthKtorConfiguration(impl: AuthKtorConfiguration) : KtorConfiguration

    @Binds
    @IntoSet
    fun bindEncodingKtorConfiguration(impl: EncodingKtorConfiguration) : KtorConfiguration

    @Binds
    @IntoSet
    fun bindContentNegotiationConfiguration(impl: ContentNegotiationConfiguration) : KtorConfiguration

    @Binds
    @IntoSet
    fun bindCacheKtorConfiguration(impl: CacheKtorConfiguration) : KtorConfiguration

    @Binds
    fun bindKtorClientBuilder(impl: KtorClientBuilderImpl) : KtorClientBuilder

    @Binds
    fun bindSignOutHandler(impl: SignOutHandlerKtor) : SignOutHandler
}