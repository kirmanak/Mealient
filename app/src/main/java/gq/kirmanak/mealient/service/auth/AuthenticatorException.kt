package gq.kirmanak.mealient.service.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle

sealed class AuthenticatorException(
    val bundle: Bundle
) : RuntimeException() {

    constructor(errorCode: Int, errorMessage: String) : this(
        Bundle().apply {
            putInt(AccountManager.KEY_ERROR_CODE, errorCode)
            putString(AccountManager.KEY_ERROR_MESSAGE, errorMessage)
        }
    )

    class UnsupportedAuthTokenType(received: String?) : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "Received auth token type = $received"
    )

    class UnsupportedAccountType(received: String?) : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "Received account type = $received"
    )

    class UnsupportedOperation(operation: String) : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION,
        errorMessage = "$operation is not supported"
    )

    class AccountNotFound(account: Account) : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "$account not found"
    )

    object NoBaseUrl : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "Base URL was not provided"
    )

    object Unauthorized : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "E-mail or password weren't correct"
    )

    object NotMealie : AuthenticatorException(
        errorCode = AccountManager.ERROR_CODE_BAD_ARGUMENTS,
        errorMessage = "Base URL must be pointing at a non Mealie server"
    )
}