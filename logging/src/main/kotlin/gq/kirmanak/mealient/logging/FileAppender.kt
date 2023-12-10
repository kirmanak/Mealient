package gq.kirmanak.mealient.logging

import android.app.Application
import gq.kirmanak.mealient.architecture.configuration.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.FileWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FileAppender @Inject constructor(
    dispatchers: AppDispatchers,
    application: Application,
) : Appender {

    private data class LogInfo(
        val logTime: Instant,
        val logLevel: LogLevel,
        val tag: String,
        val message: String,
    )

    private val fileWriter: BufferedWriter = BufferedWriter(FileWriter(application.getLogFile()))

    private val logChannel = Channel<LogInfo>(
        capacity = 100,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    private val coroutineScope = CoroutineScope(dispatchers.io + SupervisorJob())

    private val dateTimeFormatter =
        DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())

    init {
        startLogWriter()
    }

    private fun startLogWriter() {
        coroutineScope.launch {
            for (logInfo in logChannel) {
                val time = dateTimeFormatter.format(logInfo.logTime)
                val level = logInfo.logLevel.name.first()
                logInfo.message.lines().forEach {
                    fileWriter.appendLine("$time $level ${logInfo.tag}: $it")
                }
            }
        }
    }

    override fun isLoggable(logLevel: LogLevel): Boolean = true

    override fun isLoggable(logLevel: LogLevel, tag: String): Boolean = true

    override fun log(logLevel: LogLevel, tag: String, message: String) {
        val logInfo = LogInfo(
            logTime = Instant.now(),
            logLevel = logLevel,
            tag = tag,
            message = message,
        )
        logChannel.trySend(logInfo)
    }

    protected fun finalize() {
        coroutineScope.cancel("Object is being destroyed")
        fileWriter.close()
    }
}