package gq.kirmanak.mealient.service.auth

import android.accounts.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError
import gq.kirmanak.mealient.ui.auth.AuthenticatorActivity
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountAuthenticatorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authDataSource: AuthDataSource,
    private val accountParameters: AccountParameters,
    private val accountManager: AccountManager,
) : AbstractAccountAuthenticator(context) {

    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String,
        requiredFeatures: Array<out String>?,
        options: Bundle,
    ): Bundle {
        Timber.v("addAccount() called with: response = $response, accountType = $accountType, authTokenType = $authTokenType, requiredFeatures = $requiredFeatures, options = $options")

        try {
            checkAccountType(accountType)
            checkAuthTokenType(authTokenType)
        } catch (e: AuthenticatorException) {
            Timber.e(e, "addAccount: validation failure")
            return e.bundle
        }

        val intent = Intent(context, AuthenticatorActivity::class.java)
        return Bundle().apply { putParcelable(AccountManager.KEY_INTENT, intent) }
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        Timber.v("getAuthToken() called with: response = $response, account = $account, authTokenType = $authTokenType, options = $options")

        val password: String?
        val baseUrl: String?
        try {
            checkAccountType(account.type)
            checkAuthTokenType(authTokenType)
            password = accountManager.getPassword(account)
                ?: throw AuthenticatorException.AccountNotFound(account)
            baseUrl = options.getString(KEY_BASE_URL) ?: throw AuthenticatorException.NoBaseUrl
        } catch (e: AuthenticatorException) {
            Timber.e(e, "getAuthToken: validation failure")
            return e.bundle
        }

        val token = try {
            runBlocking { authDataSource.authenticate(account.name, password, baseUrl) }
        } catch (e: RuntimeException) {
            return when (e) {
                is AuthenticationError.NotMealie -> AuthenticatorException.NotMealie.bundle
                is AuthenticationError.Unauthorized -> AuthenticatorException.Unauthorized.bundle
                else -> throw NetworkErrorException(e)
            }
        }

        return Bundle().apply {
            putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            putString(AccountManager.KEY_ACCOUNT_TYPE, accountParameters.accountType)
            putString(AccountManager.KEY_AUTHTOKEN, token)
        }
    }

    // region Unsupported operations
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle {
        Timber.v("confirmCredentials() called with: response = $response, account = $account, options = $options")
        return AuthenticatorException.UnsupportedOperation("confirmCredentials").bundle
    }

    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String,
    ): Bundle? {
        Timber.v("editProperties() called with: response = $response, accountType = $accountType")
        response.onError(
            AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION,
            "editProperties is not supported"
        )
        return null
    }

    override fun getAuthTokenLabel(authTokenType: String?): String? {
        Timber.v("getAuthTokenLabel() called with: authTokenType = $authTokenType")
        return null
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        Timber.v("updateCredentials() called with: response = $response, account = $account, authTokenType = $authTokenType, options = $options")
        return AuthenticatorException.UnsupportedOperation("updateCredentials").bundle
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle {
        Timber.v("hasFeatures() called with: response = $response, account = $account, features = $features")
        return Bundle().apply { putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true) }
    }
    // end region

    private fun checkAccountType(accountType: String) {
        if (accountType != accountParameters.accountType) {
            throw AuthenticatorException.UnsupportedAccountType(accountType)
        }
    }

    private fun checkAuthTokenType(authTokenType: String) {
        if (authTokenType != accountParameters.accountType) {
            throw AuthenticatorException.UnsupportedAccountType(authTokenType)
        }
    }
}
