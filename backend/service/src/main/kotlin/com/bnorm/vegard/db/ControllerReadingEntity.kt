package com.bnorm.vegard.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object ControllerReadingTable : Table("vegard.controller_readings") {
  val controllerId = reference("controller_id", ControllerTable)
  val timestamp = timestamp("timestamp")
  val ambientTemperature = double("ambient_temperature")
  val ambientHumidity = double("ambient_humidity")
  val soilMoisture = double("soil_moisture")
}
