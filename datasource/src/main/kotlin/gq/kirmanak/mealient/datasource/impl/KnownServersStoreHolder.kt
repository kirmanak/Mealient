package gq.kirmanak.mealient.datasource.impl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.logging.Logger
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.Certificate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KnownServersStoreHolder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
) {

    val knownServersStore: KeyStore by lazy { initialiseKnownServersStore() }

    private fun initialiseKnownServersStore(): KeyStore {
        val store = KeyStore.getInstance(KeyStore.getDefaultType())
        val localTrustStoreFile = File(context.filesDir, LOCAL_TRUSTSTORE_FILENAME)
        logger.d { "Searching known-servers store at ${localTrustStoreFile.absolutePath}" }
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

    fun addCertToKnownServersStore(cert: Certificate, context: Context) {
        knownServersStore.setCertificateEntry(cert.hashCode().toString(), cert)
        context.openFileOutput(LOCAL_TRUSTSTORE_FILENAME, Context.MODE_PRIVATE).use {
            knownServersStore.store(it, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
        }
    }

    companion object {
        private const val LOCAL_TRUSTSTORE_FILENAME = "knownServers.bks"
        private const val LOCAL_TRUSTSTORE_PASSWORD = "password"
    }
}