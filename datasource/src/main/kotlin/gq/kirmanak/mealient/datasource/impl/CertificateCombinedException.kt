package gq.kirmanak.mealient.datasource.impl

import java.security.cert.*
import javax.net.ssl.SSLPeerUnverifiedException


/**
 * Exception joining all the problems that [AdvancedX509TrustManager] can find in
 * a certificate chain for a server.
 *
 *
 * This was initially created as an extension of CertificateException, but some
 * implementations of the SSL socket layer in existing devices are REPLACING the CertificateException
 * instances thrown by [javax.net.ssl.X509TrustManager.checkServerTrusted]
 * with SSLPeerUnverifiedException FORGETTING THE CAUSING EXCEPTION instead of wrapping it.
 *
 *
 * Due to this, extending RuntimeException is necessary to get that the CertificateCombinedException
 * instance reaches [AdvancedSslSocketFactory.verifyPeerIdentity].
 *
 *
 * BE CAREFUL. As a RuntimeException extensions, Java compilers do not require to handle it
 * in client methods. Be sure to use it only when you know exactly where it will go.
 *
 * @author David A. Velasco
 */
class CertificateCombinedException(val serverCert: X509Certificate) : RuntimeException() {

    var certificateExpiredException: CertificateExpiredException? = null
    var certificateNotYetValidException: CertificateNotYetValidException? = null
    var certPathValidatorException: CertPathValidatorException? = null
    var otherCertificateException: CertificateException? = null
    var sslPeerUnverifiedException: SSLPeerUnverifiedException? = null

    fun isException(): Boolean {
        return listOf(
            certificateExpiredException,
            certificateNotYetValidException,
            certPathValidatorException,
            otherCertificateException,
            sslPeerUnverifiedException
        ).any { it != null }
    }

    companion object {
        private const val serialVersionUID: Long = -8875782030758554999L
    }
}