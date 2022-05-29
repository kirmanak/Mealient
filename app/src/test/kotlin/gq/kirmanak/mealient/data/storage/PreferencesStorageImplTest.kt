package gq.kirmanak.mealient.data.storage

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import gq.kirmanak.mealient.test.HiltRobolectricTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class PreferencesStorageImplTest : HiltRobolectricTest() {

    @Inject
    lateinit var subject: PreferencesStorage

    @Test
    fun `when getValue without writes then null`() = runTest {
        assertThat(subject.getValue(subject.baseUrlKey)).isNull()
    }

    @Test(expected = IllegalStateException::class)
    fun `when requireValue without writes then throws IllegalStateException`() = runTest {
        subject.requireValue(subject.baseUrlKey)
    }

    @Test
    fun `when getValue after write then returns value`() = runTest {
        subject.storeValues(Pair(subject.baseUrlKey, "test"))
        assertThat(subject.getValue(subject.baseUrlKey)).isEqualTo("test")
    }

    @Test
    fun `when storeValue then valueUpdates emits`() = runTest {
        subject.storeValues(Pair(subject.baseUrlKey, "test"))
        assertThat(subject.valueUpdates(subject.baseUrlKey).first()).isEqualTo("test")
    }

    @Test
    fun `when remove value then getValue returns null`() = runTest {
        subject.storeValues(Pair(subject.baseUrlKey, "test"))
        subject.removeValues(subject.baseUrlKey)
        assertThat(subject.getValue(subject.baseUrlKey)).isNull()
    }
}