package gq.kirmanak.mealient.data.baseurl

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.baseurl.impl.ServerInfoStorageImpl
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServerInfoStorageImplTest {

    @MockK(relaxUnitFun = true)
    lateinit var preferencesStorage: PreferencesStorage

    lateinit var subject: ServerInfoStorage

    private val baseUrlKey = stringPreferencesKey("baseUrlKey")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = ServerInfoStorageImpl(preferencesStorage)
        every { preferencesStorage.baseUrlKey } returns baseUrlKey
    }

    @Test
    fun `when getBaseURL and preferences storage empty then null`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns null
        assertThat(subject.getBaseURL()).isNull()
    }

    @Test(expected = IllegalStateException::class)
    fun `when requireBaseURL and preferences storage empty then IllegalStateException`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns null
        subject.requireBaseURL()
    }

    @Test
    fun `when getBaseUrl and preferences storage has value then value`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns "baseUrl"
        assertThat(subject.getBaseURL()).isEqualTo("baseUrl")
    }

    @Test
    fun `when requireBaseURL and preferences storage has value then value`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns "baseUrl"
        assertThat(subject.requireBaseURL()).isEqualTo("baseUrl")
    }

    @Test
    fun `when storeBaseURL then calls preferences storage`() = runTest {
        subject.storeBaseURL("baseUrl", "v0.5.6")
        coVerify {
            preferencesStorage.baseUrlKey
            preferencesStorage.storeValues(eq(Pair(baseUrlKey, "baseUrl")))
        }
    }
}
