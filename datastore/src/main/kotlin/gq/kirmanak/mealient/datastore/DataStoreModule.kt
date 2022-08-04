package gq.kirmanak.mealient.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.datastore.recipe.AddRecipeInput
import gq.kirmanak.mealient.datastore.recipe.AddRecipeInputSerializer
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    companion object {
        const val ENCRYPTED = "encrypted"

        @Provides
        @Singleton
        fun provideAddRecipeInputStore(
            @ApplicationContext context: Context
        ): DataStore<AddRecipeInput> = DataStoreFactory.create(AddRecipeInputSerializer) {
            context.dataStoreFile("add_recipe_input")
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
}