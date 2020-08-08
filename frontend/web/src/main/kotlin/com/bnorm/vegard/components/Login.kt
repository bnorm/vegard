package com.bnorm.vegard.components

import com.bnorm.vegard.auth.useUserSession
import com.bnorm.vegard.model.Password
import com.bnorm.vegard.model.UserLoginRequest
import com.bnorm.vegard.service.useVegardService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.margin
import kotlinx.css.marginTop
import kotlinx.css.pct
import kotlinx.css.width
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import materialui.components.button.button
import materialui.components.button.enums.ButtonColor
import materialui.components.button.enums.ButtonVariant
import materialui.components.checkbox.checkbox
import materialui.components.checkbox.enums.CheckboxColor
import materialui.components.container.container
import materialui.components.container.enums.ContainerMaxWidth
import materialui.components.cssbaseline.cssBaseline
import materialui.components.formcontrol.enums.FormControlMargin
import materialui.components.formcontrol.enums.FormControlVariant
import materialui.components.formcontrollabel.formControlLabel
import materialui.components.grid.grid
import materialui.components.link.link
import materialui.components.textfield.textField
import materialui.components.typography.enums.TypographyVariant
import materialui.components.typography.typography
import materialui.styles.makeStyles
import materialui.styles.muitheme.spacing
import materialui.styles.palette.main
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.dom.div
import react.dom.form
import react.getValue
import react.rFunction
import react.setValue
import react.useState

@Suppress("FunctionName")
fun RBuilder.Login() = LOGIN {}

private interface LoginProps : RProps

private val useStyles = makeStyles<dynamic> {
  "paper" {
    marginTop = theme.spacing(8)
    display = Display.flex
    flexDirection = FlexDirection.column
    alignItems = Align.center
  }
  "avatar" {
    margin = theme.spacing(1, 1, 1, 1)
    backgroundColor = theme.palette.secondary.main
  }
  "form" {
    width = 100.pct // Fix IE 11 issue.
    marginTop = theme.spacing(1)
  }
  "submit" {
    margin = theme.spacing(3, 0, 2)
  }
}

private val LOGIN = rFunction<LoginProps>("Login") {
  val classes = useStyles()
  var email by useState("")
  var password by useState("")
  val service = useVegardService()
  val session = useUserSession()

  fun validateForm() = email.isNotEmpty() && password.isNotEmpty()
  fun handleSubmit(event: Event) {
    event.preventDefault()

    session.scope.launch {
      session.authenticating()
      runCatching {
        service.login(UserLoginRequest(email, Password(password)))
        val user = service.getMe()
        session.login(user)
      }.onFailure {
        session.logout()
      }
    }
  }

  container {
    attrs {
      component = "main"
      maxWidth = ContainerMaxWidth.xs
    }

    cssBaseline { }
    div(classes = classes.paper) {
      typography {
        attrs {
          component = "h1"
          variant = TypographyVariant.h5
        }

        +"Sign in"
      }

      form(classes = classes.form) {
        attrs {
          novalidate = true
          onSubmitFunction = { handleSubmit(it) }
        }

        textField {
          attrs {
            variant = FormControlVariant.outlined
            margin = FormControlMargin.normal
            required = true
            fullWidth = true
            id = "email"
            label { +"Email Address" }
            name = "email"
            autoComplete = "email"
            autoFocus = true
            onChangeFunction = { email = (it.target as HTMLInputElement).value }
          }
        }
        textField {
          attrs {
            variant = FormControlVariant.outlined
            margin = FormControlMargin.normal
            required = true
            fullWidth = true
            name = "password"
            label { +"Password" }
            type = InputType.password
            id = "password"
            autoComplete = "current-password"
            onChangeFunction = { password = (it.target as HTMLInputElement).value }
          }
        }
        formControlLabel {
          attrs {
            control { checkbox { attrs.value = "remember"; attrs.color = CheckboxColor.primary } }
            label { +"Remember me" }
          }
        }
        button {
          attrs {
            type = ButtonType.submit
            fullWidth = true
            variant = ButtonVariant.contained
            color = ButtonColor.primary
            className = classes.submit
            disabled = !validateForm()
          }

          +"Sign In"
        }
        grid {
          attrs.container = true

          grid {
            attrs.item = true
            attrs.xs(true)

            link {
              attrs.href = "#"
              attrs.variant = TypographyVariant.body2
              +"Forgot password?"
            }
          }
          grid {
            attrs.item = true

            link {
              attrs.href = "#"
              attrs.variant = TypographyVariant.body2
              +"Don't have an account? Sign Up"
            }
          }
        }
      }
    }
  }
}
