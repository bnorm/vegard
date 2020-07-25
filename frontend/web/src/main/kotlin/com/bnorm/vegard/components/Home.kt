package com.bnorm.vegard.components

import com.bnorm.vegard.auth.useUserSession
import com.bnorm.vegard.client.vegardClient
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerReading
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.dom.div
import react.dom.h1
import react.getValue
import react.rFunction
import react.setValue
import react.useEffect
import react.useState
import thirdparty.dxchart.components.ArgumentAxis
import thirdparty.dxchart.components.Chart
import thirdparty.dxchart.components.EventTracker
import thirdparty.dxchart.components.LineSeries
import thirdparty.dxchart.components.Tooltip
import thirdparty.dxchart.components.ValueAxis
import kotlin.js.Date

fun RBuilder.Home() = HOME {}

private interface HomeProps : RProps

private val HOME = rFunction<HomeProps>("Home") {
  val session = useUserSession()
  var controllerReadings by useState<Map<Controller, List<ControllerReading>>?>(null)
  useEffect(emptyList()) {
    GlobalScope.launch {
      val now = Date()
      val startTime = Date(now.getTime() - (15L * 24 * 60 * 60 * 1000))
      controllerReadings = vegardClient.getControllers()
        .map { async { it to vegardClient.getControllerRecords(it.id, startTime.toISOString()) } }
        .map { it.await() }
        .filter { it.second.isNotEmpty() }
        .toMap()
    }
  }

  div {
    h1 {
      +"Hello, ${session.user?.firstName}"
    }

    controllerReadingCharts(controllerReadings)
  }
}

private fun RBuilder.controllerReadingCharts(controllerReadings: Map<Controller, List<ControllerReading>>?) {
  if (controllerReadings == null) {
    div {
      +"Loading controller data..."
    }
  } else if (controllerReadings.isEmpty()) {
    div {
      +"No controller data"
    }
  } else {
    for ((_, readings) in controllerReadings) {
      data class Reading(
        val timestamp: Long,
        val soilMoisture: Double,
        val ambientTemperature: Double,
        val ambientHumidity: Double
      )

      Chart(
        data = readings
          .map {
            Reading(
              it.timestamp.getTime().toLong(),
              (1.0 - toRatio(it.soilMoisture, 480.0, 870.0)) * 100,
              it.ambientTemperature.toFahrenheit(),
              it.ambientHumidity
            )
          }
          .toTypedArray()
      ) {
        ArgumentAxis {}
        ValueAxis {}

        EventTracker {}
        Tooltip {}

        LineSeries(
          name = "Soil Moisture",
          valueField = Reading::soilMoisture,
          argumentField = Reading::timestamp
        )
        LineSeries(
          name = "Ambient Temperature",
          valueField = Reading::ambientTemperature,
          argumentField = Reading::timestamp
        )
        LineSeries(
          name = "Ambient Humidity",
          valueField = Reading::ambientHumidity,
          argumentField = Reading::timestamp
        )
      }
    }
  }
}

private fun toRatio(value: Double, min: Double, max: Double): Double = (minOf(maxOf(value, min), max) - min) / (max - min)
private fun Double.toFahrenheit() = ((this * 9.0) / 5.0) + 32.0
