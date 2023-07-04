package gq.kirmanak.mealient.datasource.impl

import android.annotation.SuppressLint
import gq.kirmanak.mealient.logging.Logger
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.cert.*
import javax.inject.Inject
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
class AdvancedX509TrustManager @Inject constructor(
    private val knownServersStoreHolder: KnownServersStoreHolder,
    private val logger: Logger
) : X509TrustManager {

    private val standardTrustManager: X509TrustManager by lazy {
        initialiseTrustManager()
    }

    private fun initialiseTrustManager(): X509TrustManager {
        val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        factory.init(null as KeyStore?)
        return factory.trustManagers
            .filterIsInstance<X509TrustManager>()
            .first()
    }

    override fun checkClientTrusted(certificates: Array<X509Certificate?>?, authType: String?) {
        standardTrustManager.checkClientTrusted(certificates, authType)
    }

    override fun checkServerTrusted(certificates: Array<X509Certificate>, authType: String?) {
        if (!isKnownServer(certificates[0])) {
            val result = CertificateCombinedException(certificates[0])
            try {
                certificates[0].checkValidity()
            } catch (c: CertificateExpiredException) {
                result.certificateExpiredException = c
            } catch (c: CertificateNotYetValidException) {
                result.certificateNotYetValidException = c
            }

            try {
                standardTrustManager.checkServerTrusted(certificates, authType)
            } catch (c: CertificateException) {
                var cause = c.cause
                var previousCause: Throwable? = null
                while (cause != null && cause != previousCause && cause !is CertPathValidatorException) {
                    previousCause = cause
                    cause = cause.cause
                }
                if (cause is CertPathValidatorException) {
                    result.certPathValidatorException = cause
                } else {
                    result.otherCertificateException = c
                }
            }

            if (result.isException()) {
                throw result
            }
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return standardTrustManager.acceptedIssuers
    }

    fun isKnownServer(cert: X509Certificate): Boolean {
        return try {
            knownServersStoreHolder.knownServersStore.getCertificateAlias(cert) != null
        } catch (e: KeyStoreException) {
            logger.e(e) { "Fail while checking certificate in the known-servers store" }
            false
        }
    }
}