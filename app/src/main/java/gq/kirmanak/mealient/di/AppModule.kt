package gq.kirmanak.mealient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.AppDb
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    companion object {

        @Provides
        @Singleton
        fun createDb(@ApplicationContext context: Context): AppDb =
            Room.databaseBuilder(context, AppDb::class.java, "app.db").build()

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("settings") }
    }

    @Binds
    @Singleton
    fun bindPreferencesStorage(preferencesStorage: PreferencesStorageImpl): PreferencesStorage
}