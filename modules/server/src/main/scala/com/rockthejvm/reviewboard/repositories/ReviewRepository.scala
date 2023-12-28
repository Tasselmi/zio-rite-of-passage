package com.rockthejvm.reviewboard.repositories

import zio.*
import com.rockthejvm.reviewboard.domain.data.*
import io.getquill.jdbczio.Quill
import io.getquill.*

trait ReviewRepository {
  def create(review: Review): Task[Review]
  def getById(id: Long): Task[Option[Review]]
  def getByCompanyId(cid: Long): Task[Option[Review]]
  def getByUserId(uid: Long): Task[Option[Review]]
  def update(id: Long, op: Review => Review): Task[Review]
  def delete(id: Long): Task[Review]
}

class ReviewRepositoryLive private (quill: Quill.Postgres[SnakeCase]) extends ReviewRepository {
  override def create(review: Review): Task[Review] =
    ZIO.fail(new RuntimeException("not implemented"))

  override def getById(id: Long): Task[Option[Review]] =
    ZIO.fail(new RuntimeException("not implemented"))

  override def getByCompanyId(cid: Long): Task[Option[Review]] =
    ZIO.fail(new RuntimeException("not implemented"))

  override def getByUserId(uid: Long): Task[Option[Review]] =
    ZIO.fail(new RuntimeException("not implemented"))

  override def update(id: Long, op: Review => Review): Task[Review] =
    ZIO.fail(new RuntimeException("not implemented"))

  override def delete(id: Long): Task[Review] =
    ZIO.fail(new RuntimeException("not implemented"))
}

object ReviewRepositoryLive {
  val layer = ZLayer {
    ZIO.serviceWith[Quill.Postgres[SnakeCase.type]](ps => ReviewRepositoryLive(ps))
  }
}
