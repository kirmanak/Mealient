package gq.kirmanak.mealient.logging

interface Appender {

    fun isLoggable(logLevel: LogLevel): Boolean

    fun isLoggable(logLevel: LogLevel, tag: String): Boolean

    fun log(logLevel: LogLevel, tag: String, message: String)

}