package gq.kirmanak.mealient.data.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.test.FakeLogger
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AnalyticsImplTest {

    @MockK(relaxUnitFun = true)
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @MockK(relaxUnitFun = true)
    lateinit var firebaseCrashlytics: FirebaseCrashlytics

    lateinit var subject: Analytics

    private val logger: Logger = FakeLogger()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = AnalyticsImpl(firebaseAnalytics, firebaseCrashlytics, logger)
    }

    @Test
    fun `when setIsEnabled expect call to analytics`() {
        subject.setIsEnabled(true)
        verify { firebaseAnalytics.setAnalyticsCollectionEnabled(eq(true)) }
    }

    @Test
    fun `when setIsEnabled expect call to crashlytics`() {
        subject.setIsEnabled(true)
        verify { firebaseCrashlytics.setCrashlyticsCollectionEnabled(eq(true)) }
    }
}