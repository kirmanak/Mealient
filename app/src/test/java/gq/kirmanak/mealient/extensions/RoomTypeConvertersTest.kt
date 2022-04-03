package gq.kirmanak.mealient.extensions

import com.google.common.truth.Truth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class RoomTypeConvertersTest {
    @Test
    fun `when localDateTimeToTimestamp then correctly converts`() {
        val input = LocalDateTime.parse("2021-11-13T15:56:33")
        val actual = RoomTypeConverters.localDateTimeToTimestamp(input)
        Truth.assertThat(actual).isEqualTo(1636818993000)
    }

    @Test
    fun `when timestampToLocalDateTime then correctly converts`() {
        val expected = LocalDateTime.parse("2021-11-13T15:58:38")
        val actual = RoomTypeConverters.timestampToLocalDateTime(1636819118000)
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when localDateToTimeStamp then correctly converts`() {
        val input = LocalDate.parse("2021-11-13")
        val actual = RoomTypeConverters.localDateToTimeStamp(input)
        Truth.assertThat(actual).isEqualTo(1636761600000)
    }

    @Test
    fun `when timestampToLocalDate then correctly converts`() {
        val expected = LocalDate.parse("2021-11-13")
        val actual = RoomTypeConverters.timestampToLocalDate(1636761600000)
        Truth.assertThat(actual).isEqualTo(expected)
    }
}