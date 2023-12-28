package gq.kirmanak.mealient.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.ui.util.LoadingHelperFactory
import gq.kirmanak.mealient.ui.util.LoadingHelperFactoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface UiModule {

    @Binds
    fun bindLoadingHelperFactory(impl: LoadingHelperFactoryImpl): LoadingHelperFactory
}