package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface EventTrackerProps : RProps {
  var onClick: ((target: TargetData) -> Unit)?
  var onPointerMove: ((target: TargetData) -> Unit)?
}

external interface TargetData {
  var location: Array<Number>
  var target: Array<SeriesRef>
  var event: Any
}

external interface SeriesRef {
  var series: String
  var point: Number
}

fun RBuilder.EventTracker(
  onClick: ((target: TargetData) -> Unit)? = null,
  onPointerMove: ((target: TargetData) -> Unit)? = null,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.EventTracker.invoke {
  attrs {
    if (onClick != null) this.onClick = onClick
    if (onPointerMove != null) this.onPointerMove = onPointerMove
  }
  block()
}
