package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.logging.LogRedactor
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialsLogRedactor @Inject constructor() : LogRedactor {

    private data class Credentials(
        val login: String,
        val password: String,
        val urlEncodedLogin: String = URLEncoder.encode(login, Charsets.UTF_8.name()),
        val urlEncodedPassword: String = URLEncoder.encode(password, Charsets.UTF_8.name()),
    )

    private val credentialsState = MutableStateFlow<Credentials?>(null)

    fun set(login: String, password: String) {
        credentialsState.value = Credentials(login, password)
    }

    fun clear() {
        credentialsState.value = null
    }

    override fun redact(message: String): String {
        val credentials = credentialsState.value ?: return message

        return message
            .replace(credentials.login, "<login>")
            .replace(credentials.urlEncodedLogin, "<login>")
            .replace(credentials.password, "<password>")
            .replace(credentials.urlEncodedPassword, "<password>")
    }
}