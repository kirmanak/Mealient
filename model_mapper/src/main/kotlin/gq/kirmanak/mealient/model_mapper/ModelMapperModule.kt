package gq.kirmanak.mealient.model_mapper

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ModelMapperModule {

    @Binds
    @Singleton
    fun bindModelMapper(impl: ModelMapperImpl): ModelMapper
}