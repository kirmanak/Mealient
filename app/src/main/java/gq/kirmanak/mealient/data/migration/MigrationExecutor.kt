package gq.kirmanak.mealient.data.migration

interface MigrationExecutor {

    val migratingFrom: Int

    suspend fun executeMigration()
}