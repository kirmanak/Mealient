package gq.kirmanak.mealient.logging

typealias MessageSupplier = () -> String

interface Logger {

    fun v(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun d(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun i(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun w(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)

    fun e(throwable: Throwable? = null, tag: String? = null, messageSupplier: MessageSupplier)
}