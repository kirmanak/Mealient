package gq.kirmanak.mealient.service.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class AccountManagerInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val accountParameters: AccountParameters,
    private val activity: Activity
) : AccountManagerInteractor {

    override fun getAccounts(): Array<Account> {
        Timber.v("getAccounts() called")
        val accounts = accountManager.getAccountsByType(accountParameters.accountType)
        Timber.v("getAccounts() returned: $accounts")
        return accounts
    }

    override suspend fun addAccount(): Account {
        Timber.v("addAccount() called")
        val bundle = accountManager.addAccount(
            accountParameters.accountType,
            accountParameters.authTokenType,
            null,
            null,
            activity,
            null,
            null
        ).await()
        return bundle.toAccount()
    }

    override suspend fun getAuthToken(account: Account): String {
        Timber.v("getAuthToken() called with: account = $account")
        val bundle = accountManager.getAuthToken(
            account,
            accountParameters.authTokenType,
            null,
            activity,
            null,
            null
        ).await()
        val receivedAccount = bundle.toAccount()
        check(account == receivedAccount) { "Received account ($receivedAccount) differs from requested ($account)" }
        val token = bundle.authToken()
        Timber.v("getAuthToken() returned: $token")
        return token
    }

    override fun accountUpdatesFlow(): Flow<Array<Account>> {
        return accountManager.accountUpdatesFlow(accountParameters.accountType)
    }
}
