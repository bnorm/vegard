package thirdparty.dxchart.components

import react.RBuilder
import react.RProps
import kotlin.reflect.KProperty1

external interface LineSeriesProps : RProps {
  var name: String
  var valueField: String
  var argumentField: String
}

fun RBuilder.LineSeries(
  name: String? = null,
  valueField: String,
  argumentField: String,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.LineSeries.invoke {
  attrs {
    if (name != null) this.name = name
    this.valueField = valueField
    this.argumentField = argumentField
  }
  block()
}

fun <T> RBuilder.LineSeries(
  name: String? = null,
  valueField: KProperty1<T, *>,
  argumentField: KProperty1<T, *>,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.LineSeries.invoke {
  attrs {
    if (name != null) this.name = name
    this.valueField = valueField.name
    this.argumentField = argumentField.name
  }
  block()
}
