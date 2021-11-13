package gq.kirmanak.mealie.data.auth.impl

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
abstract class BaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun inject() {
        hiltRule.inject()
    }
}