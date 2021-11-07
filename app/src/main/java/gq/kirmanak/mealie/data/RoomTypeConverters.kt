package gq.kirmanak.mealie.data

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object RoomTypeConverters {
    @TypeConverter
    fun instantToTimestamp(instant: Instant) = instant.toEpochMilliseconds()

    @TypeConverter
    fun timestampToInstant(timestamp: Long) = Instant.fromEpochMilliseconds(timestamp)
}