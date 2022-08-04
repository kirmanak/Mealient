package gq.kirmanak.mealient.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.datastore.recipe.AddRecipeInput
import gq.kirmanak.mealient.datastore.recipe.AddRecipeInputSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    companion object {
        @Provides
        @Singleton
        fun provideAddRecipeInputStore(
            @ApplicationContext context: Context
        ): DataStore<AddRecipeInput> = DataStoreFactory.create(AddRecipeInputSerializer) {
            context.dataStoreFile("add_recipe_input")
        }
    }
}