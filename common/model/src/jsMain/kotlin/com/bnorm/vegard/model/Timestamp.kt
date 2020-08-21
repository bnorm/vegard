package com.bnorm.vegard.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.js.Date

actual typealias Timestamp = Date

@Serializer(forClass = Timestamp::class)
internal actual object TimestampSerializer : KSerializer<Timestamp> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): Timestamp = Date(decoder.decodeString())
  override fun serialize(encoder: Encoder, value: Timestamp) {
    encoder.encodeString(value.toISOString())
  }
}
