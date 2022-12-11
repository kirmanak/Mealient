package gq.kirmanak.mealient.data.migration

interface MigrationDetector {

    suspend fun executeMigrations()
}