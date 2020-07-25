package com.bnorm.vegard.components

import com.bnorm.vegard.RNode
import kotlinx.css.Display
import kotlinx.css.Overflow
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.flexGrow
import kotlinx.css.flexShrink
import kotlinx.css.height
import kotlinx.css.overflow
import kotlinx.css.overflowY
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.css.zIndex
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import materialui.components.appbar.appBar
import materialui.components.appbar.enums.AppBarPosition
import materialui.components.cssbaseline.cssBaseline
import materialui.components.drawer.drawer
import materialui.components.drawer.enums.DrawerStyle
import materialui.components.drawer.enums.DrawerVariant
import materialui.components.toolbar.toolbar
import materialui.components.typography.enums.TypographyVariant
import materialui.components.typography.typography
import materialui.styles.makeStyles
import materialui.styles.muitheme.spacing
import materialui.styles.palette.GreyType
import materialui.styles.palette.get
import react.RBuilder
import react.RProps
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input
import react.dom.li
import react.dom.main
import react.dom.span
import react.dom.ul
import react.rFunction
import react.router.dom.routeLink

@Suppress("FunctionName")
fun RBuilder.Layout(
  toolbar: RNode,
  sidebar: RNode,
  details: RNode? = null,
  block: RBuilder.() -> Unit = {}
) = LAYOUT {
  attrs.toolbar = toolbar
  attrs.sidebar = sidebar
  attrs.details = details
  block()
}

private interface LayoutProps : RProps {
  var toolbar: RNode
  var sidebar: RNode
  var details: RNode?
}

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

private val LAYOUT = rFunction<LayoutProps>("Layout") { props ->
  val classes = useStyles()

  div(classes = classes.root) {
    cssBaseline {}

    appBar {
      attrs {
        className = classes.appBar
        position = AppBarPosition.fixed
      }
      toolbar {
        props.toolbar(this)
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
        props.sidebar(this)
      }
    }

    main(classes = classes.content) {
      toolbar {}

      props.children()
    }

    // TODO props.details?.let { div(classes = classes.sidebar) { it(this) } }
  }
}


