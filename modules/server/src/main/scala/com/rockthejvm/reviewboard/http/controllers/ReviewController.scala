package com.rockthejvm.reviewboard.http.controllers

import zio.*
import sttp.tapir.server.ServerEndpoint
import com.rockthejvm.reviewboard.http.endpoints.*
import com.rockthejvm.reviewboard.services.*

class ReviewController private (reviewService: ReviewService)
    extends BaseController
    with ReviewEndpoints {

  val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogicSuccess(crr =>
    reviewService.create(crr, 0L /* TODO: implement user id */ )
  )

  val getById: ServerEndpoint[Any, Task] =
    getByIdEndpoint.serverLogicSuccess(id => reviewService.getById(id))

  val getByCid: ServerEndpoint[Any, Task] =
    getByComanpyIdEndpoint.serverLogicSuccess(cid => reviewService.getByCompanyId(cid))

  val getByUid: ServerEndpoint[Any, Task] =
    getByUserIdEndpoint.serverLogicSuccess(uid => reviewService.getByUserId(uid))

  override val routes: List[ServerEndpoint[Any, Task]] = List(
    create,
    getById,
    getByCid,
    getByUid
  )
}

object ReviewController {
  val makeZIO: ZIO[ReviewService, Nothing, ReviewController] =
    ZIO.serviceWith[ReviewService](rs => new ReviewController(rs))
}
