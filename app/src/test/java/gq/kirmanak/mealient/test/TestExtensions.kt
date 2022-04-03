package gq.kirmanak.mealient.test

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber

fun String.toJsonResponseBody() = toResponseBody("application/json".toMediaType())

fun plantPrintLn() {
    Timber.plant(object : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            println(message)
            t?.printStackTrace()
        }
    })
}