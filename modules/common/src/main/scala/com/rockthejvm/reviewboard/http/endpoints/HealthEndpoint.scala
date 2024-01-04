package com.rockthejvm.reviewboard.http.endpoints

import sttp.tapir.*

trait HealthEndpoint {
  val healthEndpoint = endpoint
    .description("health check")
    .get
    .in("health")
    .out(plainBody[String])
}
