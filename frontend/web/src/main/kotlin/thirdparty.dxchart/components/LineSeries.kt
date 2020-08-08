package thirdparty.dxchart.components

import react.RBuilder
import react.RClass
import react.RProps
import kotlin.reflect.KProperty1

external interface LineSeries : RClass<LineSeriesProps> {
  val Path: RClass<PathSeriesProps>
}

external interface LineSeriesProps : RProps {
  var name: String
  var valueField: String
  var argumentField: String
  var scaleName: String?
  var color: String?
  var seriesComponent: RClass<PathSeriesProps>
}

external interface SeriesProps : RProps {
  var coordinates: Array<Point>
  var color: String
  var rotated: Boolean
}

external interface PathSeriesProps : SeriesProps {
  var path: (coordinates: Array<Point>) -> String
}

class Point(
  val arg: Number,
  val `val`: Number
)

fun RBuilder.LineSeries(
  valueField: String,
  argumentField: String,
  name: String? = null,
  seriesComponent: RClass<PathSeriesProps>? = null,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.LineSeries.invoke {
  attrs {
    this.valueField = valueField
    this.argumentField = argumentField
    if (name != null) this.name = name
    if (seriesComponent != null) this.seriesComponent = seriesComponent
  }
  block()
}

fun <T> RBuilder.LineSeries(
  valueField: KProperty1<T, *>,
  argumentField: KProperty1<T, *>,
  name: String? = null,
  seriesComponent: RClass<PathSeriesProps>? = null,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.LineSeries.invoke {
  attrs {
    this.valueField = valueField.name
    this.argumentField = argumentField.name
    if (name != null) this.name = name
    if (seriesComponent != null) this.seriesComponent = seriesComponent
  }
  block()
}
