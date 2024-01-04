package com.rockthejvm.reviewboard

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object App {

  def main(args: Array[String]): Unit = {
    val containerNode = dom.document.querySelector("#app")
    render(
      containerNode,
      div(
        p("This is an app"),
        p("Rock the JVM but also JS")
      )
    )
  }
}
