package gq.kirmanak.mealient.datasource

import java.security.cert.CertPathValidatorException
import java.security.cert.CertificateException
import java.security.cert.CertificateExpiredException
import java.security.cert.CertificateNotYetValidException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLPeerUnverifiedException


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