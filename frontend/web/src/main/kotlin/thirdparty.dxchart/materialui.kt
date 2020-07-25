@file:JsModule("@devexpress/dx-react-chart-material-ui")

package thirdparty.dxchart

import react.RClass
import thirdparty.dxchart.components.ArgumentAxisProps
import thirdparty.dxchart.components.ChartProps
import thirdparty.dxchart.components.LineSeriesProps
import thirdparty.dxchart.components.TooltipProps
import thirdparty.dxchart.components.ValueAxisProps

internal external val Chart: RClass<ChartProps>

internal external val LineSeries: RClass<LineSeriesProps>
internal external val SplineSeries: RClass<LineSeriesProps>

internal external val ArgumentAxis: RClass<ArgumentAxisProps>
internal external val ValueAxis: RClass<ValueAxisProps>

internal external val Tooltip: RClass<TooltipProps>
