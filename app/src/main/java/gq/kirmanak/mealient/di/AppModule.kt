package gq.kirmanak.mealient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorageImpl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    companion object {

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("settings") }

        @Provides
        @Singleton
        fun provideCoilImageLoader(
            @ApplicationContext context: Context,
            okHttpClient: OkHttpClient,
        ): ImageLoader {
            return ImageLoader.Builder(context)
                .okHttpClient(okHttpClient)
                .build()
        }
    }

    @Binds
    fun bindPreferencesStorage(preferencesStorage: PreferencesStorageImpl): PreferencesStorage
}