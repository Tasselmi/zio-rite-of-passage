package com.rockthejvm

import zio.json.{DeriveJsonCodec, JsonCodec}

case class CreateJobRequest(
    title: String,
    url: String,
    company: String
)

object CreateJobRequest {
  given codec: JsonCodec[CreateJobRequest] = DeriveJsonCodec.gen[CreateJobRequest]
}
