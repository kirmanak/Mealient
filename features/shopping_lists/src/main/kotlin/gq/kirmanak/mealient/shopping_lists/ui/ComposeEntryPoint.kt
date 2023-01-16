package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.logging.Logger

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ComposeEntryPoint {

    fun provideLogger(): Logger
}

@Composable
fun getComposeEntryPoint(): ComposeEntryPoint {
    return EntryPointAccessors.fromApplication(LocalContext.current)
}