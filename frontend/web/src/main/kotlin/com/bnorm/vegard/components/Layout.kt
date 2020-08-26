package com.bnorm.vegard.components

import com.bnorm.react.RFunction
import com.bnorm.vegard.RNode
import kotlinx.css.Display
import kotlinx.css.Overflow
import kotlinx.css.display
import kotlinx.css.flexGrow
import kotlinx.css.flexShrink
import kotlinx.css.overflow
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.css.zIndex
import materialui.components.appbar.appBar
import materialui.components.appbar.enums.AppBarPosition
import materialui.components.cssbaseline.cssBaseline
import materialui.components.drawer.drawer
import materialui.components.drawer.enums.DrawerStyle
import materialui.components.drawer.enums.DrawerVariant
import materialui.components.toolbar.toolbar
import materialui.styles.makeStyles
import materialui.styles.muitheme.spacing
import react.RBuilder
import react.dom.div
import react.dom.main

private val drawerWidth = 240.px
private val useStyles = makeStyles<dynamic> {
  "root" {
    display = Display.flex
  }
  "appBar" {
    zIndex = theme.zIndex.drawer.toInt() + 1
  }
  "drawer" {
    width = drawerWidth
    flexShrink = 0.0
  }
  "drawerPaper" {
    width = drawerWidth
  }
  "drawerContainer" {
    overflow = Overflow.auto
  }
  "content" {
    flexGrow = 1.0
    padding = theme.spacing(3).toString()
  }
}

@Suppress("FunctionName")
@RFunction
fun RBuilder.Layout(
  toolbar: RNode,
  sidebar: RNode,
  details: RNode? = null,
  block: RBuilder.() -> Unit = {}
) {
  val classes = useStyles()

  div(classes = classes.root) {
    cssBaseline {}

    appBar {
      attrs {
        className = classes.appBar
        position = AppBarPosition.fixed
      }
      toolbar {
        toolbar(this)
      }
    }

    drawer {
      attrs {
        className = classes.drawer
        variant = DrawerVariant.permanent
        classes(DrawerStyle.paper to classes.drawerPaper)
      }
      toolbar {}
      div(classes = classes.drawerContainer) {
        sidebar(this)
      }
    }

    main(classes = classes.content) {
      toolbar {}

      block()
    }
  }

  // TODO details?.let { div(classes = classes.sidebar) { it(this) } }
}
