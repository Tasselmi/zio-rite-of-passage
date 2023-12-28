package com.rockthejvm.reviewboard.repositories

import zio.*
import com.rockthejvm.reviewboard.domain.data.*
import io.getquill.jdbczio.Quill
import io.getquill.*

trait ReviewRepository {
  def create(review: Review): Task[Review]
  def getById(id: Long): Task[Option[Review]]
  def getByCompanyId(cid: Long): Task[List[Review]]
  def getByUserId(uid: Long): Task[List[Review]]
  def update(id: Long, op: Review => Review): Task[Review]
  def delete(id: Long): Task[Review]
}

class ReviewRepositoryLive private (quill: Quill.Postgres[SnakeCase]) extends ReviewRepository {
  import quill.*

  inline given reviewSchema: SchemaMeta[Review]     = schemaMeta("reviews")
  inline given reviewInsertMeta: InsertMeta[Review] = insertMeta(_.id, _.ctime, _.mtime)
  inline given reviewUpdateMeta: UpdateMeta[Review] =
    updateMeta(_.id, _.userId, _.companyId, _.ctime)

  override def create(review: Review): Task[Review] = run {
    query[Review].insertValue(lift(review)).returning(r => r)
  }

  override def getById(id: Long): Task[Option[Review]] = run {
    query[Review].filter(_.id == lift(id))
  }.map(_.headOption)

  override def getByCompanyId(cid: Long): Task[List[Review]] = run {
    query[Review].filter(_.companyId == lift(cid))
  }

  override def getByUserId(uid: Long): Task[List[Review]] = run {
    query[Review].filter(_.userId == lift(uid))
  }

  override def update(id: Long, op: Review => Review): Task[Review] = for {
    current <- getById(id)
      .someOrFail(new RuntimeException(s"update review failed: missing id $id"))
    updated <- run {
      query[Review].filter(_.id == lift(id)).updateValue(lift(op(current))).returning(r => r)
    }
  } yield updated

  override def delete(id: Long): Task[Review] = run {
    query[Review].filter(_.id == lift(id)).delete.returning(r => r)
  }
}

object ReviewRepositoryLive {
  val layer = ZLayer {
    ZIO.serviceWith[Quill.Postgres[SnakeCase.type]](ps => ReviewRepositoryLive(ps))
  }
}
