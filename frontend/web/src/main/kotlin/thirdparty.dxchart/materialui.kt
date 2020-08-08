@file:JsModule("@devexpress/dx-react-chart-material-ui")

package thirdparty.dxchart

import react.RClass
import react.RProps
import thirdparty.dxchart.components.ArgumentAxisProps
import thirdparty.dxchart.components.ChartProps
import thirdparty.dxchart.components.LegendProps
import thirdparty.dxchart.components.LineSeries
import thirdparty.dxchart.components.LineSeriesProps
import thirdparty.dxchart.components.ScatterSeries
import thirdparty.dxchart.components.TitleProps
import thirdparty.dxchart.components.TooltipProps
import thirdparty.dxchart.components.ValueAxisProps

internal external val Chart: RClass<ChartProps>

internal external val Title: RClass<TitleProps>
internal external val Legend: RClass<LegendProps>

internal external val LineSeries: LineSeries
internal external val ScatterSeries: ScatterSeries
internal external val SplineSeries: RClass<LineSeriesProps>

internal external val ArgumentAxis: RClass<ArgumentAxisProps>
internal external val ValueAxis: RClass<ValueAxisProps>

internal external val Tooltip: RClass<TooltipProps>
