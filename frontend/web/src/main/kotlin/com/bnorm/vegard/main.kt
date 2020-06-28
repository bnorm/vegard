package com.bnorm.vegard

import com.bnorm.vegard.components.App
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlin.browser.document

fun main() {
  requireAll(require.context("/", true, js("/\\.css$/")))

  document.getElementById("root")?.let {
    render(it) {
      App()
    }
  }
}
