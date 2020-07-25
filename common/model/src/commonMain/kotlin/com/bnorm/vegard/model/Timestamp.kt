package com.bnorm.vegard.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer

expect class Timestamp

@Serializer(forClass = Timestamp::class)
internal expect object TimestampSerializer : KSerializer<Timestamp>
