package com.rockthejvm.reviewboard.http.controllers

import zio.*

import scala.collection.mutable
import com.rockthejvm.reviewboard.http.endpoints.CompanyEndpoints
import com.rockthejvm.reviewboard.domain.data.Company
import sttp.tapir.server.ServerEndpoint

class CompanyController private extends BaseController with CompanyEndpoints {

  val db = mutable.Map.empty[Long, Company]

  // create
  val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogicSuccess { req =>
    ZIO.succeed {
      // create an id
      val newId = db.keys.maxOption.getOrElse(0L) + 1L
      // create a slug
      // val slug = Company.makeSlug(req.name)
      // create a company
      val newCompany = req.toCompany(newId)
      // insert the company into the 'database'
      db += (newId -> newCompany)
      // return that company
      newCompany
    }
  }

  val getAll: ServerEndpoint[Any, Task] =
    getAllEndpoint.serverLogicSuccess(_ => ZIO.succeed(db.values.toList))

  val getById: ServerEndpoint[Any, Task] = getByIdEndpoint.serverLogicSuccess { id =>
    ZIO.attempt(id.toLong).map(db.get)
  }

  override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById)
}

object CompanyController {
  val makeZIO = ZIO.succeed(new CompanyController)
}
