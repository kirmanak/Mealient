package gq.kirmanak.mealient.data.auth.impl

import android.accounts.Account
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.service.auth.AccountManagerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val accountManagerInteractor: AccountManagerInteractor,
) : AuthRepo {

    override val isAuthorizedFlow: Flow<Boolean>
        get() = accountManagerInteractor.accountUpdatesFlow()
            .map { it.firstOrNull() }
            .map { account ->
                runCatchingExceptCancel { getAuthToken(account) }
                    .onFailure { Timber.e(it, "authHeaderObservable: can't get token") }
                    .getOrNull()
            }.map { it != null }

    override suspend fun authenticate(username: String, password: String) {
        Timber.v("authenticate() called with: username = $username, password = $password")
        val account = accountManagerInteractor.addAccount(username, password)
        runCatchingExceptCancel {
            getAuthToken(account) // Try to get token to check if password is correct
        }.onFailure {
            Timber.e(it, "authenticate: can't authorize")
            removeAccount(account) // Remove account with incorrect password
        }.onSuccess {
            Timber.d("authenticate: successfully authorized")
        }.getOrThrow() // Throw error to show it to user
    }

    override suspend fun getAuthHeader(): String? {
        Timber.v("getAuthHeader() called")
        return currentAccount()
            ?.let { getAuthToken(it) }
            ?.let { AUTH_HEADER_FORMAT.format(it) }
    }

    private suspend fun getAuthToken(account: Account?): String? {
        return account?.let { accountManagerInteractor.getAuthToken(it) }
    }

    private fun currentAccount(): Account? {
        val account = accountManagerInteractor.getAccounts().firstOrNull()
        Timber.v("currentAccount() returned: $account")
        return account
    }

    override suspend fun requireAuthHeader(): String =
        checkNotNull(getAuthHeader()) { "Auth header is null when it was required" }

    override suspend fun logout() {
        Timber.v("logout() called")
        currentAccount()?.let { removeAccount(it) }
    }

    private suspend fun removeAccount(account: Account) {
        Timber.v("removeAccount() called with: account = $account")
        accountManagerInteractor.removeAccount(account)
    }

    companion object {
        private const val AUTH_HEADER_FORMAT = "Bearer %s"
    }
}