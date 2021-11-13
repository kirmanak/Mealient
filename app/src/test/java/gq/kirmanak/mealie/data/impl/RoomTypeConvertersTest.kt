package gq.kirmanak.mealie.data.impl

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class RoomTypeConvertersTest {
    @Test
    fun `when localDateTimeToTimestamp then correctly converts`() {
        val input = LocalDateTime.parse("2021-11-13T15:56:33")
        val actual = RoomTypeConverters.localDateTimeToTimestamp(input)
        assertThat(actual).isEqualTo(1636818993000)
    }

    @Test
    fun `when timestampToLocalDateTime then correctly converts`() {
        val expected = LocalDateTime.parse("2021-11-13T15:58:38")
        val actual = RoomTypeConverters.timestampToLocalDateTime(1636819118000)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when localDateToTimeStamp then correctly converts`() {
        val input = LocalDate.parse("2021-11-13")
        val actual = RoomTypeConverters.localDateToTimeStamp(input)
        assertThat(actual).isEqualTo(1636761600000)
    }

    @Test
    fun `when timestampToLocalDate then correctly converts`() {
        val expected = LocalDate.parse("2021-11-13")
        val actual = RoomTypeConverters.timestampToLocalDate(1636761600000)
        assertThat(actual).isEqualTo(expected)
    }
}