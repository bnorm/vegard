package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

interface ValueAxisProps : RProps

fun RBuilder.ValueAxis(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.ValueAxis.invoke {
  attrs {
  }
  block()
}
