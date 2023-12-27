package com.rockthejvm.reviewboard.http

import com.rockthejvm.reviewboard.http.controllers.*
import com.rockthejvm.reviewboard.services.CompanyService
import sttp.tapir.server.ServerEndpoint
import zio.{Task, ZIO}

object HttpApi {

  private def gatherRoutes(controllers: List[BaseController]) = controllers.flatMap(_.routes)

  private def makeControllers = for {
    health <- HealthController.makeZIO
    companies <- CompanyController.makeZIO

    // add new controllers here
  } yield List(health, companies)

  val endpointZIO: ZIO[CompanyService, Nothing, List[ServerEndpoint[Any, Task]]] = 
    makeControllers.map(gatherRoutes)
}
