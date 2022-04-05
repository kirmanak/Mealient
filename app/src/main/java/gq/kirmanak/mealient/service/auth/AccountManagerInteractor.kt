package gq.kirmanak.mealient.service.auth

import android.accounts.Account
import kotlinx.coroutines.flow.Flow

interface AccountManagerInteractor {

    fun getAccounts(): Array<Account>

    suspend fun addAccount(email: String, password: String): Account

    suspend fun getAuthToken(account: Account): String

    fun accountUpdatesFlow(): Flow<Array<Account>>

    suspend fun removeAccount(account: Account)
}