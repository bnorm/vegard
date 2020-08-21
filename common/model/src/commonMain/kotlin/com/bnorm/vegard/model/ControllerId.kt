package com.bnorm.vegard.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO inline class when kotlinx-serialization supports
//  https://github.com/Kotlin/kotlinx.serialization/issues/344
@Serializable
class ControllerId(
  val value: Long
) {
  @Serializer(forClass = ControllerId::class)
  companion object : KSerializer<ControllerId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ControllerId", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: ControllerId) {
      encoder.encodeLong(value.value)
    }

    override fun deserialize(decoder: Decoder): ControllerId {
      return ControllerId(decoder.decodeLong())
    }
  }

  override fun toString(): String = value.toString()
  override fun hashCode(): Int = value.hashCode()
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ControllerId) return false
    if (value != other.value) return false
    return true
  }
}
