package gq.kirmanak.mealient.data.disclaimer

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val IS_DISCLAIMER_ACCEPTED_KEY = "IS_DISCLAIMER_ACCEPTED"

class DisclaimerStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : DisclaimerStorage {

    override suspend fun isDisclaimerAccepted(): Boolean {
        Timber.v("isDisclaimerAccepted() called")
        val isAccepted = withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(IS_DISCLAIMER_ACCEPTED_KEY, false)
        }
        Timber.v("isDisclaimerAccepted() returned: $isAccepted")
        return isAccepted
    }

    override fun acceptDisclaimer() {
        Timber.v("acceptDisclaimer() called")
        sharedPreferences.edit()
            .putBoolean(IS_DISCLAIMER_ACCEPTED_KEY, true)
            .apply()
    }
}