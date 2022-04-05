package gq.kirmanak.mealient.service.auth

import android.accounts.Account
import android.accounts.AccountManager
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManagerInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val accountParameters: AccountParameters,
) : AccountManagerInteractor {

    override fun getAccounts(): Array<Account> {
        Timber.v("getAccounts() called")
        val accounts = accountManager.getAccountsByType(accountParameters.accountType)
        Timber.v("getAccounts() returned: ${accounts.contentToString()}")
        return accounts
    }

    override suspend fun addAccount(email: String, password: String): Account {
        Timber.v("addAccount() called with: email = $email, password = $password")
        val account = Account(email, accountParameters.accountType)
        removeAccount(account) // Remove account if it was created earlier
        accountManager.addAccountExplicitly(account, password, null)
        return account
    }

    override suspend fun getAuthToken(account: Account): String {
        Timber.v("getAuthToken() called with: account = $account")
        val bundle = accountManager.getAuthToken(
            account,
            accountParameters.authTokenType,
            null,
            null,
            null,
            null
        ).await()
        val receivedAccount = bundle.toAccount()
        check(account == receivedAccount) {
            "Received account ($receivedAccount) differs from requested ($account)"
        }
        val token = bundle.authToken()
        Timber.v("getAuthToken() returned: $token")
        return token
    }

    override fun accountUpdatesFlow(): Flow<Array<Account>> {
        Timber.v("accountUpdatesFlow() called")
        return accountManager.accountUpdatesFlow(accountParameters.accountType)
    }

    override suspend fun removeAccount(account: Account) {
        Timber.v("removeAccount() called with: account = $account")
        val bundle = accountManager.removeAccount(account, null, null, null).await()
        Timber.d("removeAccount: result is ${bundle.result()}")
    }
}
