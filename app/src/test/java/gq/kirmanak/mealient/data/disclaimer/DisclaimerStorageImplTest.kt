package gq.kirmanak.mealient.data.disclaimer

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DisclaimerStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: DisclaimerStorageImpl

    @Test
    fun `when isDisclaimerAccepted initially then false`() = runTest {
        assertThat(subject.isDisclaimerAccepted()).isFalse()
    }

    @Test
    fun `when isDisclaimerAccepted after accept then true`() = runTest {
        subject.acceptDisclaimer()
        assertThat(subject.isDisclaimerAccepted()).isTrue()
    }

}