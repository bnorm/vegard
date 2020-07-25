package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface EventTrackerProps : RProps

fun RBuilder.EventTracker(
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.EventTracker.invoke {
  attrs {
  }
  block()
}
