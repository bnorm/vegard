package com.bnorm.vegard.model

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlin.js.Date

actual typealias Timestamp = Date

@Serializer(forClass = Timestamp::class)
internal actual object TimestampSerializer : KSerializer<Timestamp> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor("Timestamp", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): Timestamp = Date(decoder.decodeString())
  override fun serialize(encoder: Encoder, value: Timestamp) {
    encoder.encodeString(value.toISOString())
  }
}
