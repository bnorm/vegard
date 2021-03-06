package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class ControllerReading(
  @Serializable(with = TimestampSerializer::class) val timestamp: Timestamp,
  val ambientTemperature: Double,
  val ambientHumidity: Double,
  val soilMoisture: Double
)
