package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface ChartProps : RProps {
  var data: Array<*>
}

fun <T> RBuilder.Chart(
  data: Array<T>,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Chart.invoke {
  attrs {
    this.data = data
  }
  block()
}
