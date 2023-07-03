package gq.kirmanak.mealient.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    @Singleton
    fun bindActivityUiStateController(impl: ActivityUiStateControllerImpl): ActivityUiStateController
}