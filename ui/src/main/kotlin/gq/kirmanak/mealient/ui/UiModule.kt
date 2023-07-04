package gq.kirmanak.mealient.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    fun bindActivityUiStateController(impl: ActivityUiStateControllerImpl): ActivityUiStateController
}