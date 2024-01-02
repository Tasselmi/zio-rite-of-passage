package com.rockthejvm.reviewboard.domain.data

import java.time.Instant
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class Review(
    id: Long, // PK
    companyId: Long,
    userId: Long,
    management: Int,
    culture: Int,
    salary: Int,
    benefits: Int,
    wouldRecommend: Int,
    review: String,
    ctime: Instant,
    mtime: Instant
)

object Review {
  given codec: JsonCodec[Review] = DeriveJsonCodec.gen[Review]
}
