package com.bnorm.vegard.model

import kotlinx.serialization.Serializable

@Serializable
data class ControllerReading(
  val ambientTemperature: Double,
  val ambientHumidity: Double,
  val soilMoisture: Double
)
