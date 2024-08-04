package gq.kirmanak.mealient.datasource.impl

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object UtcInstantSerializer : KSerializer<Instant> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return Instant.parseInstantOrLocalDateTime(string)
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }
}

internal fun Instant.Companion.parseInstantOrLocalDateTime(
    input: String,
    fallbackTimeZone: TimeZone = TimeZone.UTC,
): Instant = runCatching {
    parse(input)
}.attemptRecover {
    LocalDateTime.parse(input).toInstant(fallbackTimeZone)
}.getOrThrow()

private fun <T> Result<T>.attemptRecover(
    block: (Throwable) -> T
): Result<T> = when (val exception = exceptionOrNull()) {
    null -> {
        this
    }

    else -> {
        runCatching {
            block(exception)
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(exception) }
        )
    }
}