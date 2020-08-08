package thirdparty.dxchart.components

import react.RBuilder
import react.RProps

external interface TitleProps : RProps {
  var text: String
}

fun RBuilder.Title(
  text: String,
  block: RBuilder.() -> Unit = {}
) = thirdparty.dxchart.Title.invoke {
  attrs {
    this.text = text
  }
  block()
}
