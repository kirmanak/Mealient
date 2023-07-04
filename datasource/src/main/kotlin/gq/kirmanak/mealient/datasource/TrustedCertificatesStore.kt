package gq.kirmanak.mealient.datasource

import java.security.cert.Certificate

interface TrustedCertificatesStore {

    fun isTrustedCertificate(cert: Certificate): Boolean

    fun addTrustedCertificate(cert: Certificate)
}