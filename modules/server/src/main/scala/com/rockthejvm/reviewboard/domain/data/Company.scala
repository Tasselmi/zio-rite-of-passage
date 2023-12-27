package com.rockthejvm.reviewboard.domain.data

import zio.json.DeriveJsonCodec
import zio.json.JsonCodec

final case class Company(
    id: Long,
    slug: String,
    val name: String,
    url: String,
    location: Option[String] = None,
    country: Option[String] = None,
    industry: Option[String] = None,
    image: Option[String] = None,
    tags: Option[List[String]] = None
)

object Company {
  given codec: JsonCodec[Company] = DeriveJsonCodec.gen[Company]

  def makeSlug(name: String): String = name.replaceAll(" +", "-").toLowerCase
}
