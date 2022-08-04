package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.impl.AddRecipeDataSourceImpl
import gq.kirmanak.mealient.data.add.impl.AddRecipeRepoImpl
import gq.kirmanak.mealient.data.add.impl.AddRecipeService
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorageImpl
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AddRecipeModule {

    companion object {

        @Provides
        @Singleton
        fun provideAddRecipeServiceFactory(
            @Named(AUTH_OK_HTTP) okHttpClient: OkHttpClient,
            json: Json,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<AddRecipeService> {
            return RetrofitBuilder(okHttpClient, json).createServiceFactory(baseURLStorage)
        }
    }

    @Binds
    @Singleton
    fun provideAddRecipeRepo(repo: AddRecipeRepoImpl): AddRecipeRepo

    @Binds
    @Singleton
    fun bindAddRecipeDataSource(addRecipeDataSourceImpl: AddRecipeDataSourceImpl): AddRecipeDataSource

    @Binds
    @Singleton
    fun bindAddRecipeStorage(addRecipeStorageImpl: AddRecipeStorageImpl): AddRecipeStorage
}