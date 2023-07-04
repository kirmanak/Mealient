package gq.kirmanak.mealient.datasource.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.datasource.TrustedCertificatesStore
import gq.kirmanak.mealient.logging.Logger
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.cert.Certificate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrustedCertificatesStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
) : TrustedCertificatesStore {

    private val trustedCertificatesStore: KeyStore by lazy { initialiseTrustedCertificatesStore() }

    private fun initialiseTrustedCertificatesStore(): KeyStore {
        val store = KeyStore.getInstance(KeyStore.getDefaultType())
        val localTrustStoreFile = File(context.filesDir, LOCAL_TRUSTSTORE_FILENAME)
        logger.d { "Looking for a trusted certificate store at ${localTrustStoreFile.absolutePath}" }
        if (localTrustStoreFile.exists()) {
            FileInputStream(localTrustStoreFile).use {
                store.load(it, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
            }
        } else {
            // next is necessary to initialize an empty KeyStore instance
            store.load(null, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
        }
        return store
    }

    override fun isTrustedCertificate(cert: Certificate): Boolean {
        return try {
            trustedCertificatesStore.getCertificateAlias(cert) != null
        } catch (e: KeyStoreException) {
            logger.e(e) { "Fail while checking certificate in the known-servers store" }
            false
        }
    }

    override fun addTrustedCertificate(cert: Certificate) {
        trustedCertificatesStore.setCertificateEntry(cert.hashCode().toString(), cert)
        context.openFileOutput(LOCAL_TRUSTSTORE_FILENAME, Context.MODE_PRIVATE).use {
            trustedCertificatesStore.store(it, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
        }
    }

    companion object {
        private const val LOCAL_TRUSTSTORE_FILENAME = "trustedCertificates.bks"
        private const val LOCAL_TRUSTSTORE_PASSWORD = "password"
    }
}