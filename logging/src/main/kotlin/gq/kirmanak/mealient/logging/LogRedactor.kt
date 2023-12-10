package gq.kirmanak.mealient.logging

interface LogRedactor {

    fun redact(message: String): String
}