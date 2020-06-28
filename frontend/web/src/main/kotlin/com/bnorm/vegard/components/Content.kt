package com.bnorm.vegard.components

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

@Suppress("FunctionName")
fun RBuilder.Content() {
  child(CONTENT) {
    attrs {

    }
  }
}

private interface ContentProps : RProps

private val CONTENT = functionalComponent<ContentProps> { props ->
}
