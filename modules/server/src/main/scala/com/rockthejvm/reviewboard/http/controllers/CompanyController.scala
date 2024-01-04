package com.rockthejvm.reviewboard.http.controllers

import zio.*
import scala.collection.mutable
import sttp.tapir.server.ServerEndpoint
import com.rockthejvm.reviewboard.domain.data.*
import com.rockthejvm.reviewboard.services.*
import com.rockthejvm.reviewboard.http.endpoints.*

class CompanyController private (service: CompanyService)
    extends BaseController
    with CompanyEndpoints {

  // create
  val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogicSuccess { req =>
    service.create(req)
  }

  val getAll: ServerEndpoint[Any, Task] =
    getAllEndpoint.serverLogicSuccess(_ => service.getAll)

  val getById: ServerEndpoint[Any, Task] = getByIdEndpoint.serverLogicSuccess { id =>
    ZIO.attempt(id.toLong).flatMap(service.getById).catchSome { case _: NumberFormatException =>
      service.getBySlug(id)
    }
  }

  override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById)
}

object CompanyController {
  val makeZIO: ZIO[CompanyService, Nothing, CompanyController] =
    ZIO.serviceWith[CompanyService](cs => new CompanyController(cs))

  val makeZIO_v2: ZIO[CompanyService, Nothing, CompanyController] =
    ZIO.serviceWithZIO[CompanyService](cs => ZIO.succeed(new CompanyController(cs)))

  val makeZIO_v3: ZIO[CompanyService, Nothing, CompanyController] = for {
    service <- ZIO.service[CompanyService]
  } yield new CompanyController(service)
}
