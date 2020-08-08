package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface LegendProps : RProps {
}

fun RBuilder.Legend(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Legend.invoke {
  attrs {
  }
  block()
}
