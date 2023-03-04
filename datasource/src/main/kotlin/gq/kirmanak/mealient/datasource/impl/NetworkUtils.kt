package gq.kirmanak.mealient.datasource.impl

import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException

object NetworkUtils
{

    /**
     * Returns the local store of reliable server certificates, explicitly accepted by the user.
     *
     *
     * Returns a KeyStore instance with empty content if the local store was never created.
     *
     *
     * Loads the store from the storage environment if needed.
     *
     * @param context Android context where the operation is being performed.
     * @return KeyStore instance with explicitly-accepted server certificates.
     * @throws KeyStoreException        When the KeyStore instance could not be created.
     * @throws IOException              When an existing local trust store could not be loaded.
     * @throws NoSuchAlgorithmException When the existing local trust store was saved with an unsupported algorithm.
     * @throws CertificateException     When an exception occurred while loading the certificates from the local
     * trust store.
     */

    private const val LOCAL_TRUSTSTORE_FILENAME = "knownServers.bks"
    private const val LOCAL_TRUSTSTORE_PASSWORD = "password"

    private var mKnownServersStore: KeyStore? = null


    @Throws(KeyStoreException::class, IOException::class, NoSuchAlgorithmException::class, CertificateException::class)
    fun getKnownServersStore(): KeyStore {
        if (mKnownServersStore == null) {
            //mKnownServersStore = KeyStore.getInstance("BKS");
            mKnownServersStore = KeyStore.getInstance(KeyStore.getDefaultType())
            /*val localTrustStoreFile = File(context.getFilesDir(), LOCAL_TRUSTSTORE_FILENAME)
            /*//logger.d { "Searching known-servers store at $localTrustStoreFile.getAbsolutePath() " }*/
            if (localTrustStoreFile.exists()) {
                val `in`: InputStream = FileInputStream(localTrustStoreFile)
                try {
                    mKnownServersStore?.load(`in`, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
                } finally {
                    `in`.close()
                }
            } else {
                // next is necessary to initialize an empty KeyStore instance
                mKnownServersStore?.load(null, LOCAL_TRUSTSTORE_PASSWORD.toCharArray())
            }*/
        }
        return mKnownServersStore!!
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, CertificateException::class, IOException::class)
    fun addCertToKnownServersStore(cert: Certificate) {
        val knownServers: KeyStore = getKnownServersStore()
        knownServers.setCertificateEntry(Integer.toString(cert.hashCode()), cert)
        /*context.openFileOutput(LOCAL_TRUSTSTORE_FILENAME, Context.MODE_PRIVATE).use { fos ->
            knownServers.store(
                fos,
                LOCAL_TRUSTSTORE_PASSWORD.toCharArray()
            )
        }*/
    }

}