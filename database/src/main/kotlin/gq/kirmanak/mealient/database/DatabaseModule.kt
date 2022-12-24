package gq.kirmanak.mealient.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.ShoppingListsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    companion object {
        @Provides
        @Singleton
        fun createDb(@ApplicationContext context: Context): AppDb =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigrationFrom(2)
                .build()

        @Provides
        @Singleton
        fun provideRecipeDao(db: AppDb): RecipeDao = db.recipeDao()

        @Provides
        @Singleton
        fun provideShoppingListsDao(db: AppDb): ShoppingListsDao = db.shoppingListsDao()
    }
}