package com.rockthejvm.reviewboard

import scala.util.Try
import com.raquo.airstream.ownership.OneTimeOwner
import com.raquo.airstream.timing.PeriodicStream
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.HTMLDivElement
import com.rockthejvm.reviewboard.components.*
import frontroute.LinkHandler

object App {

  val app: ReactiveHtmlElement[HTMLDivElement] = div(
    Header(),
    Router()
  ).amend(LinkHandler.bind)

  def main(args: Array[String]): Unit = {
    import Tutorial.*

    val containerNode = dom.document.querySelector("#app")
    render(
      containerNode,
      app

      /** laminar crash course
        */
//      div(
//        // modifiers
//        // CSS class
//        // styles
//        styleAttr := "color:green",
//        // onClick
//        // children
//        p("This is an app"),
//        p("Rock the JVM but also JS"),
//        staticContent,
//        timeUpdated,
//        clickUpdated,
//        clicksQueried,
//        clicksVar
//      )
    )
  }
}
// dom.querySelector(...).appendChild(...)

// reactive variables
object Tutorial {
  val staticContent = div(
    // modifiers
    // CSS class
    // styles
    styleAttr := "color:red",
    // onClick
    // children
    p("This is an app"),
    p("Rock the JVM but also JS")
  )

  /** EventStream -- produce values of the same type
    */
  val ticks: PeriodicStream[Int] = EventStream.periodic(1000)
  // subscription -- Airstream
  given timer: OneTimeOwner = new OneTimeOwner(() => ())
  val subscription = ticks.addObserver(
    new Observer[Int] {
      override def onTry(nextValue: Try[Int]): Unit = ()
      override def onError(err: Throwable): Unit    = ()
      override def onNext(nextValue: Int): Unit     = dom.console.log(s"ticks: $nextValue")
    }
  )
  scala.scalajs.js.timers.setTimeout(10000)(subscription.kill())

  val timeUpdated = div(
    span("Time since loaded: "),
    child <-- ticks.map(num => s"$num seconds")
  )

  /** EventBus -- like EventStreams, but you can push new elements to the stream
    */
  val clickEvents = EventBus[Int]()
  val clickUpdated = div(
    span("clicks since loaded: "),
    button(
      `type` := "button",
      "Add a Click",
      styleAttr := "display:block",
      onClick.map(_ => 1) --> clickEvents
    ),
    child <-- clickEvents.events.scanLeft(0)(_ + _).map(num => s"$num clicks")
  )

  /** Signal -- similar to EventStreams, but they have a "current value" (state)
    */
  val countSignal = clickEvents.events.scanLeft(0)(_ + _).observe(using timer)
  val queryEvents = EventBus[Unit]()
  val clicksQueried = div(
    span("clicks since loaded: "),
    button(
      `type` := "button",
      "Add a Click",
      styleAttr := "display:block",
      onClick.map(_ => 1) --> clickEvents
    ),
    button(
      `type` := "button",
      "Refresh count",
      styleAttr := "display:block",
      onClick.mapTo(()) --> queryEvents
    ),
    child <-- queryEvents.events.map(_ => countSignal.now())
  )

  /** Var -- reactive variable
    */
  val countVar = Var[Int](0)
  val clicksVar = div(
    span("clicks so far: "),
    child <-- countVar.signal.map(_.toString),
    button(
      `type` := "button",
      "Click Me",
      styleAttr := "display:block",
      // onClick --> countVar.updater((current, mouseEvent) => current + 1)
      // onClick --> countVar.writer.contramap(event => countVar.now() + 1)
      onClick --> (_ => countVar.set(countVar.now() + 1))
    )
  )

  /** NO STATE | WITH STATE
    * --------------------------+----------------------------- read EventStream | Signal
    * --------------------------+----------------------------- write EventBus | Var
    * --------------------------+-----------------------------
    */
}
