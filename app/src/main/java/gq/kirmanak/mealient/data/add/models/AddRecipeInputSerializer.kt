package gq.kirmanak.mealient.data.add.models

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AddRecipeInputSerializer : Serializer<AddRecipeInput> {
    override val defaultValue: AddRecipeInput = AddRecipeInput.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AddRecipeInput = try {
        AddRecipeInput.parseFrom(input)
    } catch (e: InvalidProtocolBufferException) {
        throw CorruptionException("Can't read proto file", e)
    }

    override suspend fun writeTo(t: AddRecipeInput, output: OutputStream) = t.writeTo(output)
}