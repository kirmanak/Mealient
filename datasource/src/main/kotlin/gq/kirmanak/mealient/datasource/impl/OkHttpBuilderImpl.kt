package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.CacheBuilder
import gq.kirmanak.mealient.datasource.LocalInterceptor
import gq.kirmanak.mealient.datasource.OkHttpBuilder
import gq.kirmanak.mealient.logging.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

class OkHttpBuilderImpl @Inject constructor(
    private val cacheBuilder: CacheBuilder,
    // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>,
    private val localInterceptors: Set<@JvmSuppressWildcards LocalInterceptor>,
    private val logger: Logger
) : OkHttpBuilder {

    override fun buildOkHttp(): OkHttpClient {
        logger.v { "buildOkHttp() was called with cacheBuilder = $cacheBuilder, interceptors = $interceptors, localInterceptors = $localInterceptors" }

        val trustManager = AdvancedX509TrustManager(NetworkUtils.getKnownServersStore(), logger)

        val sslContext = buildSSLContext()
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder().apply {
            localInterceptors.forEach(::addInterceptor)
            interceptors.forEach(::addNetworkInterceptor)
            sslSocketFactory(sslSocketFactory, trustManager)
            cache(cacheBuilder.buildCache())
        }.build()
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun buildSSLContext(): SSLContext {
        return try {
            SSLContext.getInstance(TlsVersion.TLS_1_3.javaName)
        } catch (tlsv13Exception: NoSuchAlgorithmException) {
            try {
                logger.w {"TLSv1.3 is not supported in this device; falling through TLSv1.2" }
                SSLContext.getInstance(TlsVersion.TLS_1_2.javaName)
            } catch (tlsv12Exception: NoSuchAlgorithmException) {
                try {
                    logger.w {"TLSv1.2 is not supported in this device; falling through TLSv1.1" }
                    SSLContext.getInstance(TlsVersion.TLS_1_1.javaName)
                } catch (tlsv11Exception: NoSuchAlgorithmException) {
                    logger.w {"TLSv1.1 is not supported in this device; falling through TLSv1.0" }
                    SSLContext.getInstance(TlsVersion.TLS_1_0.javaName)
                    // should be available in any device; see reference of supported protocols in
                    // http://developer.android.com/reference/javax/net/ssl/SSLSocket.html
                }
            }
        }
    }

}