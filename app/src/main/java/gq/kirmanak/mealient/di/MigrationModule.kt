package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.data.migration.From24AuthMigrationExecutor
import gq.kirmanak.mealient.data.migration.MigrationDetector
import gq.kirmanak.mealient.data.migration.MigrationDetectorImpl
import gq.kirmanak.mealient.data.migration.MigrationExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MigrationModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindFrom24AuthMigrationExecutor(from24AuthMigrationExecutor: From24AuthMigrationExecutor): MigrationExecutor

    @Binds
    @Singleton
    fun bindMigrationDetector(migrationDetectorImpl: MigrationDetectorImpl): MigrationDetector
}