package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface TooltipProps : RProps

fun RBuilder.Tooltip(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Tooltip.invoke {
  attrs {
  }
  block()
}
