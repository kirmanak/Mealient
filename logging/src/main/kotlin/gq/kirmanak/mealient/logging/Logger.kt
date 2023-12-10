package gq.kirmanak.mealient.logging

import android.content.Context
import java.io.File

typealias MessageSupplier = () -> String

private const val LOG_FILE_NAME = "log.txt"

interface Logger {

    fun v(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun d(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun i(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun w(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun e(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)
}

fun Context.getLogFile(): File {
    return File(filesDir, LOG_FILE_NAME)
}