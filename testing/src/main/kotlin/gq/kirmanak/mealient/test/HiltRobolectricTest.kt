package gq.kirmanak.mealient.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import gq.kirmanak.mealient.logging.Logger
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE)
abstract class HiltRobolectricTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    protected val logger: Logger = FakeLogger()

    @Before
    fun inject() {
        hiltRule.inject()
    }
}