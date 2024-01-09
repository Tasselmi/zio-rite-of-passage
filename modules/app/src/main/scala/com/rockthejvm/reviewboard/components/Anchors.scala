package com.rockthejvm.reviewboard.components

import org.scalajs.dom
import com.raquo.laminar.api.L.given
import com.raquo.laminar.api.L.*

/**
 * n.
 * 锚；支柱，靠山；商场；<美>新闻节目主持人；<英，非正式>（汽车的）刹车
 *
 * v.
 * 抛锚，泊（船）；使稳固，使固定；成为支柱；<美>主持（新闻节目）；使扎根，使基于；支持，保护
 *
 * adj.
 * 末棒的，最后一棒的
 */
object Anchors {

  def renderNavLink(text: String, location: String, cssClass: String = "") = a(
    href := location,
    cls  := cssClass,
    text
  )
}
