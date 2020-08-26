package com.bnorm.vegard.components

import com.bnorm.react.RFunction
import com.bnorm.vegard.model.Controller
import com.bnorm.vegard.model.ControllerReading
import com.bnorm.vegard.service.useVegardService
import com.bnorm.vegard.useMainScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.dom.div
import react.getValue
import react.setValue
import react.useEffect
import react.useState
import thirdparty.dxchart.components.ArgumentAxis
import thirdparty.dxchart.components.Chart
import thirdparty.dxchart.components.EventTracker
import thirdparty.dxchart.components.LineSeries
import thirdparty.dxchart.components.Title
import thirdparty.dxchart.components.Tooltip
import thirdparty.dxchart.components.ValueAxis
import kotlin.js.Date

@Suppress("FunctionName")
@RFunction
fun RBuilder.ControllerChart(
  controller: Controller,
  startTime: Date,
  endTime: Date = Date()
) {
  val service = useVegardService()
  val scope = useMainScope(name = "ControllerChartScope")

  var startTime by useState(startTime)
  var endTime by useState(endTime)
  var controllerReadings by useState<List<ControllerReading>?>(null)
  useEffect(listOf(startTime, endTime)) {
    scope.launch {
      controllerReadings = service.getControllerRecords(controller.id, startTime.toISOString(), endTime.toISOString())
    }
  }

  val readings = controllerReadings
  if (readings == null) {
    div { +"Loading controller data..." }
  } else if (readings.isEmpty()) {
    div { +"No controller data" }
  } else {
    Chart(
      data = readings
        .map {
          ReadingView(
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

      LineSeries(
        name = "Soil Moisture",
        valueField = ReadingView::soilMoisture,
        argumentField = ReadingView::timestamp
      )
      LineSeries(
        name = "Ambient Temperature",
        valueField = ReadingView::ambientTemperature,
        argumentField = ReadingView::timestamp
      )
      LineSeries(
        name = "Ambient Humidity",
        valueField = ReadingView::ambientHumidity,
        argumentField = ReadingView::timestamp
      )

      Title(text = controller.macAddress) {}
      // TODO Legend {} causes line series to be invisible

      EventTracker {}
      Tooltip {}
    }
  }
}

private fun toRatio(value: Double, min: Double, max: Double): Double = (minOf(maxOf(value, min), max) - min) / (max - min)
private fun Double.toFahrenheit() = ((this * 9.0) / 5.0) + 32.0

private data class ReadingView(
  val timestamp: Long,
  val soilMoisture: Double,
  val ambientTemperature: Double,
  val ambientHumidity: Double
)
