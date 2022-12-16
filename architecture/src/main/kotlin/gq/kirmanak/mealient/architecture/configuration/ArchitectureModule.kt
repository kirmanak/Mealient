package gq.kirmanak.mealient.architecture.configuration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ArchitectureModule {

    @Binds
    @Singleton
    fun bindAppDispatchers(appDispatchersImpl: AppDispatchersImpl): AppDispatchers
}