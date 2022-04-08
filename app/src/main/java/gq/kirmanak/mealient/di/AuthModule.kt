package gq.kirmanak.mealient.di

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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
import gq.kirmanak.mealient.data.auth.impl.AuthService
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    companion object {
        const val ENCRYPTED = "encrypted"

        @Provides
        @Singleton
        fun provideAuthServiceFactory(
            @Named(NO_AUTH_OK_HTTP) okHttpClient: OkHttpClient,
            json: Json,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<AuthService> {
            return RetrofitBuilder(okHttpClient, json).createServiceFactory(baseURLStorage)
        }

        @Provides
        @Singleton
        fun provideAccountManager(@ApplicationContext context: Context): AccountManager {
            return AccountManager.get(context)
        }

        @Provides
        @Singleton
        @Named(ENCRYPTED)
        fun provideEncryptedSharedPreferences(
            @ApplicationContext applicationContext: Context,
        ): SharedPreferences {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
            return EncryptedSharedPreferences.create(
                ENCRYPTED,
                mainKeyAlias,
                applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
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
    fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage
}
