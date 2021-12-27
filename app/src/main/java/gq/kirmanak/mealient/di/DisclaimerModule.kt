package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorageImpl

@Module
@InstallIn(SingletonComponent::class)
interface DisclaimerModule {
    @Binds
    fun provideDisclaimerStorage(disclaimerStorageImpl: DisclaimerStorageImpl): DisclaimerStorage
}