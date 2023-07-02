package gq.kirmanak.mealient.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealient.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    companion object {

        @Provides
        @Singleton
        fun provideAccountManager(@ApplicationContext context: Context): AccountManager {
            return AccountManager.get(context)
        }
    }

    @Binds
    @Singleton
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo

    @Binds
    @Singleton
    fun bindAuthProvider(authRepo: AuthRepoImpl): AuthenticationProvider

    @Binds
    @Singleton
    fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

    @Binds
    @Singleton
    fun bindShoppingListsAuthRepo(impl: AuthRepoImpl): ShoppingListsAuthRepo
}
