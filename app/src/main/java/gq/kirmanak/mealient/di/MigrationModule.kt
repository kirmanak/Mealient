package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.data.migration.From24AuthMigrationExecutor
import gq.kirmanak.mealient.data.migration.From30MigrationExecutor
import gq.kirmanak.mealient.data.migration.MigrationDetector
import gq.kirmanak.mealient.data.migration.MigrationDetectorImpl
import gq.kirmanak.mealient.data.migration.MigrationExecutor

@Module
@InstallIn(SingletonComponent::class)
interface MigrationModule {

    @Binds
    @IntoSet
    fun bindFrom24AuthMigrationExecutor(from24AuthMigrationExecutor: From24AuthMigrationExecutor): MigrationExecutor

    @Binds
    @IntoSet
    fun bindFrom30MigrationExecutor(impl: From30MigrationExecutor): MigrationExecutor

    @Binds
    fun bindMigrationDetector(migrationDetectorImpl: MigrationDetectorImpl): MigrationDetector
}