package gq.kirmanak.mealie.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MealieModule {
    companion object {
        @Provides
        fun createDb(@ApplicationContext context: Context): MealieDb {
            return Room.databaseBuilder(context, MealieDb::class.java, "mealie.db").build()
        }
    }
}