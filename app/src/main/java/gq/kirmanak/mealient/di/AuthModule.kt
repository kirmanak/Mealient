package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealient.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl
import gq.kirmanak.mealient.data.auth.impl.CredentialsLogRedactor
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.logging.LogRedactor
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo

    @Binds
    fun bindAuthProvider(authRepo: AuthRepoImpl): AuthenticationProvider

    @Binds
    fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

    @Binds
    fun bindShoppingListsAuthRepo(impl: AuthRepoImpl): ShoppingListsAuthRepo

    @Binds
    @IntoSet
    fun bindCredentialsLogRedactor(impl: CredentialsLogRedactor): LogRedactor
}
