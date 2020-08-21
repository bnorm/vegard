package com.bnorm.vegard.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@RequiresOptIn
annotation class PasswordUsage

// TODO inline class when kotlinx-serialization supports
//  https://github.com/Kotlin/kotlinx.serialization/issues/344
@Serializable
class Password(
  @property:PasswordUsage
  val value: String
) {
  @Serializer(forClass = Password::class)
  companion object : KSerializer<Password> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Password", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Password) {
      @OptIn(PasswordUsage::class)
      encoder.encodeString(value.value)
//      throw UnsupportedOperationException("Unable to serialize a raw password")
    }

    override fun deserialize(decoder: Decoder): Password {
      return Password(decoder.decodeString())
    }
  }

  override fun toString(): String = "*****"
}
