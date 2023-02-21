package gq.kirmanak.mealient.database

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.RecipeStorage
import gq.kirmanak.mealient.database.recipe.RecipeStorageImpl
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsDao
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorage
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DatabaseModule {

    companion object {
        @Provides
        @Singleton
        fun createDb(@ApplicationContext context: Context): AppDb =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        @Singleton
        fun provideRecipeDao(db: AppDb): RecipeDao = db.recipeDao()

        @Provides
        @Singleton
        fun provideShoppingListsDao(db: AppDb): ShoppingListsDao = db.shoppingListsDao()
    }

    @Binds
    @Singleton
    fun bindShoppingListsStorage(impl: ShoppingListsStorageImpl): ShoppingListsStorage

    @Binds
    @Singleton
    fun provideRecipeStorage(recipeStorageImpl: RecipeStorageImpl): RecipeStorage
}