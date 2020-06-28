package com.bnorm.vegard.components

import com.bnorm.vegard.UserContext
import com.bnorm.vegard.session
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.h1
import react.dom.input
import react.dom.li
import react.dom.nav
import react.dom.span
import react.dom.ul
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.routeLink

@Suppress("FunctionName")
fun RBuilder.Home() {
  child(HOME) {

  }
}

private interface HomeProps : RProps

private val HOME = functionalComponent<HomeProps> {
  UserContext.Consumer { (state, _) ->
    val session = state.session ?: return@Consumer

    browserRouter {

      route(path = "/home") {
        div {
          nav(classes = "navbar navbar-expand-lg navbar-dark bg-dark") {
            routeLink(className = "navbar-brand", to = "/") { +"Navbar" }
            button(classes = "navbar-toggler", type = ButtonType.button) {
              attrs["data-toggle"] = "collapse"
              attrs["data-target"] = "#navbarSupportContent"
              attrs["aria-controls"] = "navbarSupportContent"
              attrs["aria-expand"] = "false"
              attrs["aria-label"] = "Toggle navigation"

              span(classes = "navbar-toggler-icon") {}
            }

            div(classes = "collapse navbar-collapse") {
              ul(classes = "navbar-nav mr-auto") {
                li(classes = "nav-item active") {
                  routeLink(className = "nav-link", to = "/home") { +"Home" }
                }
              }

              form(classes = "form-inline my-2 my-lg-0") {
                attrs.onSubmitFunction = { it.preventDefault() } // TODO: Make search do nothing for right now
                input(classes = "form-control mr-sm-2", type = InputType.search) {
                  attrs.placeholder = "Search"
                  attrs["aria-label"] = "Search"
                }
                button(classes = "btn btn-outline-success my-2 my-sm-0 mr-sm-2", type = ButtonType.submit) {
                  +"Search"
                }
              }

              Logout()
            }
          }

          h1 {
            +"Hello, ${session.user.firstName}"
          }
        }
      }
      redirect(from = "/", to = "/home", exact = true)
    }
  }
}
