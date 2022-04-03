package gq.kirmanak.mealient.service.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManager.*
import android.accounts.AccountManagerFuture
import android.accounts.OnAccountsUpdateListener
import android.os.Build
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

internal const val KEY_BASE_URL = "mealientBaseUrl"

internal suspend fun <T> AccountManagerFuture<T>.await(): T = withContext(Dispatchers.IO) { result }

internal fun Bundle.toAccount(): Account = Account(accountName(), accountType())

internal fun Bundle.accountType(): String = string(KEY_ACCOUNT_TYPE) { "Account type is null" }

internal fun Bundle.accountName(): String = string(KEY_ACCOUNT_NAME) { "Account name is null" }

internal fun Bundle.authToken(): String = string(KEY_AUTHTOKEN) { "Auth token is null" }

private fun Bundle.string(key: String, error: () -> String) = checkNotNull(getString(key), error)

@OptIn(ExperimentalCoroutinesApi::class)
internal fun AccountManager.accountUpdatesFlow(vararg types: String): Flow<Array<Account>> =
    callbackFlow {
        Timber.v("accountUpdatesFlow() called")
        val listener = OnAccountsUpdateListener { accounts ->
            Timber.d("accountUpdatesFlow: updated accounts = $accounts")
            val filtered = accounts.filter { types.contains(it.type) }.toTypedArray()
            Timber.d("accountUpdatesFlow: filtered accounts = $filtered")
            trySendBlocking(filtered)
                .onSuccess { Timber.d("accountUpdatesFlow: sent accounts update") }
                .onFailure { Timber.e(it, "accountUpdatesFlow: failed to send update") }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addOnAccountsUpdatedListener(listener, null, true, types)
        } else {
            addOnAccountsUpdatedListener(listener, null, true)
        }
        awaitClose {
            Timber.d("accountUpdatesFlow: cancelled")
            removeOnAccountsUpdatedListener(listener)
        }
    }
