package gq.kirmanak.mealient.data.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AnalyticsImplTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @MockK(relaxUnitFun = true)
    lateinit var firebaseCrashlytics: FirebaseCrashlytics

    lateinit var subject: Analytics

    @Before
    override fun setUp() {
        super.setUp()
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