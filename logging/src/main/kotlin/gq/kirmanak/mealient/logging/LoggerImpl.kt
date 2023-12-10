package gq.kirmanak.mealient.logging

import android.util.Log
import java.util.regex.Pattern
import javax.inject.Inject

class LoggerImpl @Inject constructor(
    private val appenders: Set<@JvmSuppressWildcards Appender>,
    private val redactors: Set<@JvmSuppressWildcards LogRedactor>,
) : Logger {

    override fun v(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        log(LogLevel.VERBOSE, tag, messageSupplier, throwable)
    }

    override fun d(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        log(LogLevel.DEBUG, tag, messageSupplier, throwable)
    }

    override fun i(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        log(LogLevel.INFO, tag, messageSupplier, throwable)
    }

    override fun w(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        log(LogLevel.WARNING, tag, messageSupplier, throwable)
    }

    override fun e(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        log(LogLevel.ERROR, tag, messageSupplier, throwable)
    }

    private fun log(
        logLevel: LogLevel,
        tag: String?,
        messageSupplier: MessageSupplier,
        t: Throwable?
    ) {
        var logTag: String? = null
        var message: String? = null
        for (appender in appenders) {
            if (appender.isLoggable(logLevel).not()) continue

            logTag = logTag ?: tag ?: Throwable().stackTrace
                .first { element -> !IGNORED_CLASSES.any { element.className.contains(it) } }
                .let(::createStackElementTag)

            if (appender.isLoggable(logLevel, logTag).not()) continue

            message = message ?: buildLogMessage(messageSupplier, t)

            appender.log(logLevel, logTag, message)
        }
    }

    private fun buildLogMessage(
        messageSupplier: MessageSupplier,
        t: Throwable?
    ): String {
        var message = messageSupplier() + createStackTrace(t)
        for (redactor in redactors) {
            message = redactor.redact(message)
        }
        return message
    }

    private fun createStackTrace(throwable: Throwable?): String =
        throwable?.let { Log.getStackTraceString(it) }
            ?.takeUnless { it.isBlank() }
            ?.let { "\n" + it }
            .orEmpty()

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag
    }

    companion object {
        private val IGNORED_CLASSES = listOf(Logger::class.java.name, LoggerImpl::class.java.name)
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }
}