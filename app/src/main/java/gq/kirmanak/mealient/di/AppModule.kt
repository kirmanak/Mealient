package gq.kirmanak.mealient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.analytics.Analytics
import gq.kirmanak.mealient.data.analytics.AnalyticsImpl
import gq.kirmanak.mealient.data.configuration.BuildConfiguration
import gq.kirmanak.mealient.data.configuration.BuildConfigurationImpl
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorageImpl
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
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
            FirebaseAnalytics.getInstance(context)

        @Provides
        @Singleton
        fun provideFirebaseCrashlytics(): FirebaseCrashlytics =
            FirebaseCrashlytics.getInstance()
    }

    @Binds
    @Singleton
    fun bindPreferencesStorage(preferencesStorage: PreferencesStorageImpl): PreferencesStorage

    @Binds
    @Singleton
    fun bindBuildConfiguration(buildConfigurationImpl: BuildConfigurationImpl): BuildConfiguration

    @Binds
    @Singleton
    fun bindAnalytics(analyticsImpl: AnalyticsImpl): Analytics
}