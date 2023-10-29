package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.logging.Logger
import okhttp3.TlsVersion
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
            SSLContext.getInstance(TlsVersion.TLS_1_3.javaName)
        }.recoverCatching {
            logger.w { "TLSv1.3 is not supported in this device; falling through TLSv1.2" }
            SSLContext.getInstance(TlsVersion.TLS_1_2.javaName)
        }.recoverCatching {
            logger.w { "TLSv1.2 is not supported in this device; falling through TLSv1.1" }
            SSLContext.getInstance(TlsVersion.TLS_1_1.javaName)
        }.recoverCatching {
            logger.w { "TLSv1.1 is not supported in this device; falling through TLSv1.0" }
            // should be available in any device; see reference of supported protocols in
            // http://developer.android.com/reference/javax/net/ssl/SSLSocket.html
            SSLContext.getInstance(TlsVersion.TLS_1_0.javaName)
        }.getOrThrow()
    }
}