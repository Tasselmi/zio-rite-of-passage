package com.rockthejvm

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Job(
    id: Long,
    title: String,
    url: String,
    company: String
)

object Job {
  given codec: JsonCodec[Job] = DeriveJsonCodec.gen[Job]
}
