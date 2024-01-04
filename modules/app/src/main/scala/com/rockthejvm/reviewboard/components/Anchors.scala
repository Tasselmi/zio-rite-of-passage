package com.rockthejvm.reviewboard.components

import org.scalajs.dom
import com.raquo.laminar.api.L.given
import com.raquo.laminar.api.L.*

object Anchors {

  def renderNavLink(text: String, location: String, cssClass: String = "") = a(
    href := location,
    cls  := cssClass,
    text
  )
}
