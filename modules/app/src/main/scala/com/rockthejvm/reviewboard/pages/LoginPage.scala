package com.rockthejvm.reviewboard.pages

import com.raquo.laminar.api.L.given
import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.HTMLDivElement

object LoginPage {

  case class State(email: String = "", password: String = "")

  val state = Var(State())

  def apply() =
    div(
      cls := "row",
      div(
        cls := "col-md-5 p-0",
        div(
          cls := "logo"
          // TODO logo
        )
      ),
      div(
        cls := "col-md-7",
        // right
        div(
          cls := "form-section",
          div(cls := "top-section", h1(span("Log In"))),
          div(
            cls := "page-status-errors",
            "This is an error"
          ),
          div(
            cls := "page-status-success",
            child.text <-- state.signal.map(_.toString)
          ),
          form(
            nameAttr := "signin",
            cls      := "form",
            idAttr   := "form",
            // an input of type text
            renderInput(
              "Email",
              "email-input",
              "text",
              "Your Email",
              true,
              (st, em) => st.copy(email = em)
            ),
            // an input of type password
            renderInput(
              "Password",
              "password-input",
              "password",
              "Your Password",
              true,
              (st, pwd) => st.copy(password = pwd)
            ),
            button(
              `type` := "button",
              "Log In"
            )
          )
        )
      )
    )

  def renderInput(
      name: String,
      uid: String,
      kind: String,
      placeHolder: String,
      isRequired: Boolean,
      updateFunc: (State, String) => State
  ): ReactiveHtmlElement[HTMLDivElement] = div(
    cls := "row",
    div(
      cls := "col-md-12",
      div(
        cls := "form-input",
        label(
          forId := uid,
          cls   := "form-label",
          if (isRequired) span("*") else span(),
          name
        ),
        input(
          `type`      := kind,
          cls         := "form-control",
          idAttr      := uid,
          placeholder := placeHolder,
          onInput.mapToValue --> state.updater(updateFunc)
        )
      )
    )
  )
}
