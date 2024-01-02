package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*
import com.rockthejvm.reviewboard.domain.data.Review
import com.rockthejvm.reviewboard.http.requests.CreateReviewRequest

trait ReviewEndpoints {
  // post /reviews ---- create a review
  val createEndpoint = endpoint
    .tag("reviews")
    .name("create")
    .description("add a review for a company")
    .in("reviews")
    .post
    .in(jsonBody[CreateReviewRequest])
    .out(jsonBody[Review])

  // get /reviews/id  --- get review by id
  val getByIdEndpoint = endpoint
    .description("get a review by its id")
    .in("reviews" / path[Long]("id"))
    .get
    .out(jsonBody[Option[Review]])

  // get /reviews/company/id  --- get review by company id
  val getByComanpyIdEndpoint = endpoint
    .description("get reviews by company id")
    .in("reviews" / "company" / path[Long]("cid"))
    .get
    .out(jsonBody[List[Review]])

  // get /reviews/user/id  --- get review by user id
  val getByUserIdEndpoint = endpoint
    .description("get reviews by user id")
    .in("reviews" / "user" / path[Long]("uid"))
    .get
    .out(jsonBody[List[Review]])
}
