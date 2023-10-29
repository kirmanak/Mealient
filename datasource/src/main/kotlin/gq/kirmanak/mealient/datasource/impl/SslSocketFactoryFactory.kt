package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

internal class SslSocketFactoryFactory @Inject constructor(
    private val advancedX509TrustManager: AdvancedX509TrustManager,
    private val logger: Logger,
) {

    fun create(): SSLSocketFactory {
        val sslContext = buildSSLContext()
        sslContext.init(null, arrayOf<TrustManager>(advancedX509TrustManager), null)
        return sslContext.socketFactory
    }

    private fun buildSSLContext(): SSLContext {
        return runCatching {
            SSLContext.getInstance("TLSv1.3")
        }.recoverCatching {
            logger.w { "TLSv1.3 is not supported in this device; falling through TLSv1.2" }
            SSLContext.getInstance("TLSv1.2")
        }.recoverCatching {
            logger.w { "TLSv1.2 is not supported in this device; falling through TLSv1.1" }
            SSLContext.getInstance("TLSv1.1")
        }.recoverCatching {
            logger.w { "TLSv1.1 is not supported in this device; falling through TLSv1.0" }
            // should be available in any device; see reference of supported protocols in
            // http://developer.android.com/reference/javax/net/ssl/SSLSocket.html
            SSLContext.getInstance("TLSv1")
        }.getOrThrow()
    }
}