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
class CertificateCombinedException(private val x509Certificate: X509Certificate) : RuntimeException() {

    private var mServerCert: X509Certificate? = null
    private var mHostInUrl: String? = null
    private var mCertificateExpiredException: CertificateExpiredException? = null
    private var mCertificateNotYetValidException: CertificateNotYetValidException? = null
    private var mCertPathValidatorException: CertPathValidatorException? = null
    private var mOtherCertificateException: CertificateException? = null
    private var mSslPeerUnverifiedException: SSLPeerUnverifiedException? = null

    init {
        mServerCert = x509Certificate
    }

    fun getServerCertificate(): X509Certificate? {
        return mServerCert
    }

    fun getHostInUrl(): String? {
        return mHostInUrl
    }

    fun setHostInUrl(host: String?) {
        mHostInUrl = host
    }

    fun getCertificateExpiredException(): CertificateExpiredException? {
        return mCertificateExpiredException
    }

    fun setCertificateExpiredException(c: CertificateExpiredException?) {
        mCertificateExpiredException = c
    }

    fun getCertificateNotYetValidException(): CertificateNotYetValidException? {
        return mCertificateNotYetValidException
    }

    fun setCertificateNotYetException(c: CertificateNotYetValidException?) {
        mCertificateNotYetValidException = c
    }

    fun getCertPathValidatorException(): CertPathValidatorException? {
        return mCertPathValidatorException
    }

    fun setCertPathValidatorException(c: CertPathValidatorException?) {
        mCertPathValidatorException = c
    }

    fun getOtherCertificateException(): CertificateException? {
        return mOtherCertificateException
    }

    fun setOtherCertificateException(c: CertificateException?) {
        mOtherCertificateException = c
    }

    fun getSslPeerUnverifiedException(): SSLPeerUnverifiedException? {
        return mSslPeerUnverifiedException
    }

    fun setSslPeerUnverifiedException(s: SSLPeerUnverifiedException?) {
        mSslPeerUnverifiedException = s
    }

    fun isException(): Boolean {
        return (mCertificateExpiredException != null ||
                mCertificateNotYetValidException != null ||
                mCertPathValidatorException != null ||
                mOtherCertificateException != null ||
                mSslPeerUnverifiedException != null)
    }

    fun isRecoverable(): Boolean {
        return (mCertificateExpiredException != null ||
                mCertificateNotYetValidException != null ||
                mCertPathValidatorException != null ||
                mSslPeerUnverifiedException != null)
    }

    companion object {
        /**
         * Generated - to refresh every time the class changes
         */
        private val serialVersionUID: Long = -8875782030758554999L
    }
}