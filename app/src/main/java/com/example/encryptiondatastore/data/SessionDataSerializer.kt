package com.example.encryptiondatastore.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.encryptiondatastore.data.model.SessionData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Plain JSON serializer for [SessionData].
 *
 * Encryption is handled externally by [AeadSerializer] (from datastore-tink),
 * which wraps this serializer in [DataStoreModule].
 */
object SessionDataSerializer : Serializer<SessionData> {

    override val defaultValue: SessionData = SessionData()

    override suspend fun readFrom(input: InputStream): SessionData {
        return try {
            Json.decodeFromString(SessionData.serializer(), input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to deserialize SessionData", e)
        }
    }

    override suspend fun writeTo(t: SessionData, output: OutputStream) {
        output.write(Json.encodeToString(SessionData.serializer(), t).encodeToByteArray())
    }
}
