package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.logging.Logger
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.cert.*
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Kotlin Implementation of https://github.com/owncloud/android-library
* Custom X509TrustManager implementation that allows the app to trust additional SSL/TLS certificates
* in addition to the system's default trusted certificates.
*
* @param knownServersKeyStore The KeyStore containing the custom trusted certificates.
*/

class AdvancedX509TrustManager (
    private val mKnownServersKeyStore: KeyStore,
    private val logger: Logger) : X509TrustManager
{

    private val serialVersionUID = -8875782030758554999L
    private val standardTrustManager: X509TrustManager

    init {
        val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        factory.init(null as KeyStore?)
        standardTrustManager = findX509TrustManager(factory)

    }

    /**
     * Locates the first X509TrustManager provided by a given TrustManagerFactory
     *
     * @param factory TrustManagerFactory to inspect in the search for a X509TrustManager
     * @return The first X509TrustManager found in factory.
     */
    private fun findX509TrustManager(factory: TrustManagerFactory): X509TrustManager {
        for (tm in factory.trustManagers) {
            if (tm is X509TrustManager) {
                return tm
            }
        }
        throw IllegalStateException("No X509TrustManager found")
    }

    /**
     * @see X509TrustManager.checkClientTrusted
     */
    @Throws(CertificateException::class)
    override fun checkClientTrusted(certificates: Array<X509Certificate>, authType: String) {
        standardTrustManager.checkClientTrusted(certificates, authType)
    }

    /**
     * @see X509TrustManager.checkServerTrusted
     */
    @Throws(CertificateException::class)
    override fun checkServerTrusted(certificates: Array<X509Certificate>, authType: String) {
        if (!isKnownServer(certificates[0])) {
            val result = CertificateCombinedException(certificates[0])
            try {
                certificates[0].checkValidity()
            } catch (c: CertificateExpiredException) {
                result.setCertificateExpiredException(c)
            } catch (c: CertificateNotYetValidException) {
                result.setCertificateNotYetException(c)
            }


            try {
                standardTrustManager.checkServerTrusted(certificates, authType)
            } catch (c: CertificateException) {
                var cause = c.cause
                var previousCause: Throwable? = null
                while (cause != null && cause != previousCause && !(cause is CertPathValidatorException)) {
                    previousCause = cause
                    cause = cause.cause
                }
                if (cause is CertPathValidatorException) {
                    result.setCertPathValidatorException(cause)
                } else {
                    result.setOtherCertificateException(c)
                }
            }

            if (result.isException()) {
                throw result
            }
        }
    }

    /**
     * @see X509TrustManager.getAcceptedIssuers
     */
    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return standardTrustManager.acceptedIssuers
    }

/**
 * Determines whether the specified certificate is trusted according to the custom KeyStore.
 *
 * @param cert The certificate to check.
 * @return True if the certificate is trusted, false
*/
    private fun isKnownServer(cert: X509Certificate?): Boolean {
    return try {
        mKnownServersKeyStore.getCertificateAlias(cert) != null
    } catch (e: KeyStoreException) {
        logger.e { "isKnownServer: Fail while checking certificate in the known-servers store" }
        false
    }
}

}