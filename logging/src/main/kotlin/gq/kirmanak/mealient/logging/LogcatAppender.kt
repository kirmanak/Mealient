package gq.kirmanak.mealient.logging

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogcatAppender @Inject constructor() : Appender {

    private val isLoggable: Boolean by lazy { BuildConfig.DEBUG }

    override fun isLoggable(logLevel: LogLevel): Boolean = isLoggable

    override fun isLoggable(logLevel: LogLevel, tag: String): Boolean = isLoggable

    override fun log(logLevel: LogLevel, tag: String, message: String) {
        if (message.length < MAX_LOG_LENGTH) {
            Log.println(logLevel.priority, tag, message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                Log.println(logLevel.priority, tag, part)
                i = end
            } while (i < newline)
            i++
        }
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
    }
}

private val LogLevel.priority: Int
    get() = when (this) {
        LogLevel.VERBOSE -> Log.VERBOSE
        LogLevel.DEBUG -> Log.DEBUG
        LogLevel.INFO -> Log.INFO
        LogLevel.WARNING -> Log.WARN
        LogLevel.ERROR -> Log.ERROR
    }