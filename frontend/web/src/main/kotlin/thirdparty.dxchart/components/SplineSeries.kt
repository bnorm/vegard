package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface SplineSeriesProps : RProps {
  var name: String
  var valueField: String
  var argumentField: String
}

fun RBuilder.SplineSeries(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.SplineSeries.invoke {
  attrs {
  }
  block()
}
