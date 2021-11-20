package gq.kirmanak.mealient.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.impl.OkHttpBuilder
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
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
        fun createSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        @Provides
        @Singleton
        fun createOkHttp(okHttpBuilder: OkHttpBuilder): OkHttpClient =
            okHttpBuilder.buildOkHttp()

        @Provides
        @Singleton
        fun createJson(): Json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }
}