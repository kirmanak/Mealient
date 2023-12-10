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
import java.io.IOException
import java.io.Writer
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_LOG_FILE_SIZE = 1024 * 1024 * 10L // 10 MB

@Singleton
internal class FileAppender @Inject constructor(
    private val application: Application,
    dispatchers: AppDispatchers,
) : Appender {

    private data class LogInfo(
        val logTime: Instant,
        val logLevel: LogLevel,
        val tag: String,
        val message: String,
    )

    private val fileWriter: Writer? = createFileWriter()

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

    private fun createFileWriter(): Writer? {
        val file = application.getLogFile()
        if (file.length() > MAX_LOG_FILE_SIZE) {
            file.delete()
        }

        val writer = try {
            FileWriter(file, /* append = */ true)
        } catch (e: IOException) {
            return null
        }

        return BufferedWriter(writer)
    }

    private fun startLogWriter() {
        if (fileWriter == null) {
            return
        }

        coroutineScope.launch {
            for (logInfo in logChannel) {
                val time = dateTimeFormatter.format(logInfo.logTime)
                val level = logInfo.logLevel.name.first()
                logInfo.message.lines().forEach {
                    try {
                        fileWriter.appendLine("$time $level ${logInfo.tag}: $it")
                    } catch (e: IOException) {
                        // Ignore
                    }
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
        try {
            fileWriter?.close()
        } catch (e: IOException) {
            // Ignore
        }
    }
}