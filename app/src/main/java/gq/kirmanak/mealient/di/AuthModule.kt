package gq.kirmanak.mealient.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealient.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealient.data.auth.impl.AuthService
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import gq.kirmanak.mealient.service.auth.AccountManagerInteractor
import gq.kirmanak.mealient.service.auth.AccountManagerInteractorImpl
import gq.kirmanak.mealient.service.auth.AccountParameters
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    companion object {

        @Provides
        @Singleton
        fun provideAuthServiceFactory(retrofitBuilder: RetrofitBuilder,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<AuthService> = retrofitBuilder.createServiceFactory(baseURLStorage)

        @Provides
        @Singleton
        fun provideAccountManager(@ApplicationContext context: Context): AccountManager {
            return AccountManager.get(context)
        }

        @Provides
        @Singleton
        fun provideAccountType(@ApplicationContext context: Context) = AccountParameters(
            accountType = context.getString(R.string.account_type),
            authTokenType = context.getString(R.string.auth_token_type),
        )
    }

    @Binds
    @Singleton
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo

    @Binds
    @Singleton
    fun bindAccountManagerInteractor(
        accountManagerInteractorImpl: AccountManagerInteractorImpl
    ): AccountManagerInteractor
}
