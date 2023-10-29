package gq.kirmanak.mealient.data.baseurl

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import gq.kirmanak.mealient.data.baseurl.impl.ServerInfoStorageImpl
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.test.AuthImplTestData.TEST_BASE_URL
import gq.kirmanak.mealient.test.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ServerInfoStorageTest : BaseUnitTest() {

    @MockK(relaxUnitFun = true)
    lateinit var preferencesStorage: PreferencesStorage

    lateinit var subject: ServerInfoStorage

    private val baseUrlKey = stringPreferencesKey("baseUrlKey")

    @Before
    override fun setUp() {
        super.setUp()
        subject = ServerInfoStorageImpl(preferencesStorage)
        every { preferencesStorage.baseUrlKey } returns baseUrlKey
    }

    @Test
    fun `when preferences storage empty expect getBaseURL return null`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns null
        assertThat(subject.getBaseURL()).isNull()
    }

    @Test
    fun `when preferences storage has value expect getBaseUrl return value`() = runTest {
        coEvery { preferencesStorage.getValue(eq(baseUrlKey)) } returns TEST_BASE_URL
        assertThat(subject.getBaseURL()).isEqualTo(TEST_BASE_URL)
    }

    @Test
    fun `when storeBaseURL expect call to preferences storage`() = runTest {
        subject.storeBaseURL(TEST_BASE_URL)
        coVerify {
            preferencesStorage.storeValues(
                eq(Pair(baseUrlKey, TEST_BASE_URL)),
            )
        }
    }
}
