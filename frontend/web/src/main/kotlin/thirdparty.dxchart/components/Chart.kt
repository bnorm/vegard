package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface ChartProps : RProps {
  var data: Array<*>
}

fun RBuilder.Chart(
  data: Array<*>,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Chart.invoke {
  attrs {
    this.data = data
  }
  block()
}
