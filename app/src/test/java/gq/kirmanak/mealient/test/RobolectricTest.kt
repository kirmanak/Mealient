package gq.kirmanak.mealient.test

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, manifest = Config.NONE)
abstract class RobolectricTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun setupTimber() = plantPrintLn()
    }
}