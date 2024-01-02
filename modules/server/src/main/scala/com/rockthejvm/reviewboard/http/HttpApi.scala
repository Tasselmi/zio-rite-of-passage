package com.rockthejvm.reviewboard.http

import com.rockthejvm.reviewboard.http.controllers.*
import com.rockthejvm.reviewboard.services.CompanyService
import sttp.tapir.server.ServerEndpoint
import zio.*

object HttpApi {

  private def gatherRoutes(controllers: List[BaseController]) = controllers.flatMap(_.routes)

  private def makeControllers = for {
    health <- HealthController.makeZIO
    companies <- CompanyController.makeZIO
    reviews <- ReviewController.makeZIO
    // add new controllers here
  } yield List(health, companies, reviews)

  val endpointsZIO = 
    makeControllers.map(gatherRoutes)
}
