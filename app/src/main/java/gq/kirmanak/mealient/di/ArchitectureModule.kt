package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import gq.kirmanak.mealient.data.configuration.AppDispatchers
import gq.kirmanak.mealient.data.configuration.AppDispatchersImpl
import gq.kirmanak.mealient.data.configuration.BuildConfigurationImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ArchitectureModule {

    @Binds
    @Singleton
    fun bindBuildConfiguration(buildConfigurationImpl: BuildConfigurationImpl): BuildConfiguration

    @Binds
    @Singleton
    fun bindAppDispatchers(appDispatchersImpl: AppDispatchersImpl): AppDispatchers
}