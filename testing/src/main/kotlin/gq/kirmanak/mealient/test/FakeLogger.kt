package gq.kirmanak.mealient.test

import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.logging.MessageSupplier

class FakeLogger : Logger {
    override fun v(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        print("V", throwable, messageSupplier)
    }

    override fun d(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        print("D", throwable, messageSupplier)
    }

    override fun i(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        print("I", throwable, messageSupplier)
    }

    override fun w(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        print("W", throwable, messageSupplier)
    }

    override fun e(throwable: Throwable?, tag: String?, messageSupplier: MessageSupplier) {
        print("E", throwable, messageSupplier)
    }

    private fun print(
        level: String,
        throwable: Throwable?,
        messageSupplier: MessageSupplier,
    ) {
        println("$level ${messageSupplier()}. ${throwable?.stackTraceToString().orEmpty()}")
    }
}