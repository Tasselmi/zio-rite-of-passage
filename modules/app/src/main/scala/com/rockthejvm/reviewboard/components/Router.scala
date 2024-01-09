package com.rockthejvm.reviewboard.components

import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.L.given
import frontroute.*
import com.rockthejvm.reviewboard.pages.*

object Router {

  def apply() = mainTag(
    routes(
      div(
        cls := "container-fluid",
        // potential children
//        pathEnd { // localhost:1234 or localhost:1234/
//          div("main page")
//        },
//        path("companies") { // localhost:1234/companies
//          div("companies page")
//        }
        (pathEnd | path("companies")) {
          CompaniesPage()
        },
        path("login") {
          LoginPage()
        },
        path("signup") {
          SignUpPage()
        },
        noneMatched {
          NotFoundPage()
        }
      )
    )
  )
}
