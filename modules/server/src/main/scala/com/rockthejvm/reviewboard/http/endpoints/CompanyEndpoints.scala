package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.jsonBody
import com.rockthejvm.reviewboard.http.requests.CreateCompanyRequest
import com.rockthejvm.reviewboard.domain.data.Company

trait CompanyEndpoints {

  val createEndpoint = endpoint
    .description("create a listing for a company")
    .in("companies")
    .post
    .in(jsonBody[CreateCompanyRequest])
    .out(jsonBody[Company])

  val getAllEndpoint = endpoint
    .description("get all company listings")
    .in("companies")
    .get
    .out(jsonBody[List[Company]])

  val getByIdEndpoint = endpoint
    .description("get company by its id or maybe by slug")
    .in("companies" / path[String]("id"))
    .get
    .out(jsonBody[Option[Company]])
}
