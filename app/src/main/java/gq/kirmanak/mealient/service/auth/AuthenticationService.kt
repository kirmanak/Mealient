package gq.kirmanak.mealient.service.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationService : Service() {

    @Inject
    lateinit var accountAuthenticatorImpl: AccountAuthenticatorImpl

    override fun onBind(intent: Intent?): IBinder? = accountAuthenticatorImpl.iBinder
}