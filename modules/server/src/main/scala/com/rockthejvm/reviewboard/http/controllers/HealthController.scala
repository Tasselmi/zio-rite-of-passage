package com.rockthejvm.reviewboard.http.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import com.rockthejvm.reviewboard.http.endpoints.*

class HealthController private extends BaseController with HealthEndpoint {

  val health = healthEndpoint.serverLogicSuccess[Task](_ => ZIO.succeed("ALL GOOD!"))

  override val routes: List[ServerEndpoint[Any, Task]] = List(health)
}

object HealthController {
  val makeZIO = ZIO.succeed(new HealthController)
}
