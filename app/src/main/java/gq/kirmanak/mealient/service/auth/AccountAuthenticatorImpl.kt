package gq.kirmanak.mealient.service.auth

import android.accounts.*
import android.content.Context
import android.os.Bundle
import dagger.hilt.android.qualifiers.ApplicationContext
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.service.auth.AuthenticatorException.*
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

    private val accountType: String
        get() = accountParameters.accountType
    private val authTokenType: String
        get() = accountParameters.authTokenType

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle?
    ): Bundle {
        Timber.v("getAuthToken() called with: response = $response, account = $account, authTokenType = $authTokenType, options = $options")

        val password = try {
            checkAccountType(account.type)
            checkAuthTokenType(authTokenType)
            accountManager.getPassword(account) ?: throw AccountNotFound(account)
        } catch (e: AuthenticatorException) {
            Timber.e(e, "getAuthToken: validation failure")
            return e.bundle
        }

        val token = runCatchingExceptCancel {
            runBlocking { authDataSource.authenticate(account.name, password) }
        }.getOrElse {
            return when (it) {
                is NetworkError.NotMealie -> NotMealie.bundle
                is NetworkError.Unauthorized -> Unauthorized.bundle
                else -> throw NetworkErrorException(it)
            }
        }

        return Bundle().apply {
            putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
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
        return UnsupportedOperation("confirmCredentials").bundle
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String,
        requiredFeatures: Array<out String>?,
        options: Bundle?,
    ): Bundle {
        Timber.v("addAccount() called with: response = $response, accountType = $accountType, authTokenType = $authTokenType, requiredFeatures = $requiredFeatures, options = $options")
        return UnsupportedOperation("addAccount").bundle
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
        return UnsupportedOperation("updateCredentials").bundle
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
        if (accountType != this.accountType) {
            throw UnsupportedAccountType(accountType)
        }
    }

    private fun checkAuthTokenType(authTokenType: String) {
        if (authTokenType != this.authTokenType) {
            throw UnsupportedAuthTokenType(authTokenType)
        }
    }
}
