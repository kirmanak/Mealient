package gq.kirmanak.mealient.data.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val logger: Logger,
) : Analytics {

    override fun setIsEnabled(enabled: Boolean) {
        logger.v { "setIsEnabled() called with: enabled = $enabled" }
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }
}