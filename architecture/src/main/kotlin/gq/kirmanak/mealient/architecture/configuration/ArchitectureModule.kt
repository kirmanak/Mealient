package gq.kirmanak.mealient.architecture.configuration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ArchitectureModule {

    @Binds
    fun bindAppDispatchers(appDispatchersImpl: AppDispatchersImpl): AppDispatchers
}