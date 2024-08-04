package gq.kirmanak.mealient.datasource.impl

import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class UtcInstantSerializerTest {

    @Test
    fun parseLocalDateTime() {
        val actual = Instant.parseInstantOrLocalDateTime(
            input = "2024-08-04T15:29:00",
            fallbackTimeZone = FixedOffsetTimeZone(UtcOffset(2))
        )
        val expected = LocalDateTime(
            year = 2024,
            monthNumber = 8,
            dayOfMonth = 4,
            hour = 15,
            minute = 29,
            second = 0,
            nanosecond = 0,
        ).toInstant(UtcOffset(2))
        assertEquals(expected, actual)
    }

    @Test
    fun parseZonedDateTime() {
        val actual = Instant.parseInstantOrLocalDateTime(
            input = "1922-03-09T06:04:15.987654321+03:00"
        )
        val expected = LocalDateTime(
            year = 1922,
            monthNumber = 3,
            dayOfMonth = 9,
            hour = 6,
            minute = 4,
            second = 15,
            nanosecond = 987654321
        ).toInstant(UtcOffset(3))
        assertEquals(expected, actual)
    }

    @Test
    fun parseNamedZonedDateTime() {
        val actual = Instant.parseInstantOrLocalDateTime(
            input = "2023-09-07T11:38:03.123456789Z",
            fallbackTimeZone = FixedOffsetTimeZone(UtcOffset(10))
        )
        val expected = LocalDateTime(
            year = 2023,
            monthNumber = 9,
            dayOfMonth = 7,
            hour = 11,
            minute = 38,
            second = 3,
            nanosecond = 123456789
        ).toInstant(TimeZone.UTC)
        assertEquals(expected, actual)
    }

    @Test
    fun rethrowsOriginalExceptionOnFailure() {
        try {
            Instant.parseInstantOrLocalDateTime("2023-09-07T11:38:03.1234567890Z")
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Failed to parse an instant from '2023-09-07T11:38:03.1234567890Z'",
                e.message
            )
        }
    }
}