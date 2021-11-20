package gq.kirmanak.mealient.data.impl

import androidx.room.TypeConverter
import kotlinx.datetime.*

object RoomTypeConverters {
    @TypeConverter
    fun localDateTimeToTimestamp(localDateTime: LocalDateTime) =
        localDateTime.toInstant(TimeZone.UTC).toEpochMilliseconds()

    @TypeConverter
    fun timestampToLocalDateTime(timestamp: Long) =
        Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC)

    @TypeConverter
    fun localDateToTimeStamp(date: LocalDate) =
        localDateTimeToTimestamp(date.atTime(0, 0))

    @TypeConverter
    fun timestampToLocalDate(timestamp: Long) =
        timestampToLocalDateTime(timestamp).date
}