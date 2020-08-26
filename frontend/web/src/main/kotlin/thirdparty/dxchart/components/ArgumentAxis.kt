package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface ArgumentAxisProps : RProps

fun RBuilder.ArgumentAxis(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.ArgumentAxis.invoke {
  attrs {
  }
  block()
}
