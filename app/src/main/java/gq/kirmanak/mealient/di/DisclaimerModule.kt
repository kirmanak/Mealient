package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DisclaimerModule {

    @Binds
    @Singleton
    fun provideDisclaimerStorage(disclaimerStorageImpl: DisclaimerStorageImpl): DisclaimerStorage
}