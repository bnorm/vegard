package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface TooltipProps : RProps {
  var targetItem: SeriesRef
  var onTargetItemChange: (target: SeriesRef) -> Unit
}

fun RBuilder.Tooltip(
  targetItem: SeriesRef? = null,
  onTargetItemChange: ((target: SeriesRef) -> Unit)? = null,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Tooltip.invoke {
  attrs {
    if (targetItem != null) this.targetItem = targetItem
    if (onTargetItemChange != null) this.onTargetItemChange = onTargetItemChange
  }
  block()
}
