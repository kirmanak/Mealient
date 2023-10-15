package gq.kirmanak.mealient.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import gq.kirmanak.mealient.logging.Logger
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE, sdk = [Config.NEWEST_SDK])
abstract class HiltRobolectricTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var logger: Logger

    @Before
    fun inject() {
        hiltRule.inject()
    }
}