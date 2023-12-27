package com.rockthejvm.reviewboard.repositories

import zio.*
import io.getquill.*
import io.getquill.jdbczio.Quill
import com.rockthejvm.reviewboard.domain.data.*

trait CompanyRepository {
  def create(company: Company): Task[Company]
  def update(id: Long, op: Company => Company): Task[Company]
  def delete(id: Long): Task[Company]
  def getById(id: Long): Task[Option[Company]]
  def getBySlug(slug: String): Task[Option[Company]]
  def get: Task[List[Company]]
}

class CompanyRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends CompanyRepository {
  import quill.*

  inline given schema: SchemaMeta[Company]  = schemaMeta[Company]("companies")
  inline given insMeta: InsertMeta[Company] = insertMeta[Company](_.id)
  inline given upMeta: UpdateMeta[Company]  = updateMeta[Company](_.id)

  override def create(company: Company): Task[Company] = run {
    query[Company].insertValue(lift(company)).returning(r => r)
  }

  override def update(id: Long, op: Company => Company): Task[Company] = for {
    current <- getById(id).someOrFail(new RuntimeException(s"could not update: missing id $id"))
    updated <- run {
      query[Company].filter(_.id == lift(id)).updateValue(lift(op(current))).returning(r => r)
    }
  } yield updated

  override def delete(id: Long): Task[Company] = run {
    query[Company].filter(_.id == lift(id)).delete.returning(r => r)
  }

  override def getById(id: Long): Task[Option[Company]] = run {
    query[Company].filter(_.id == lift(id))
  }.map(_.headOption)

  override def getBySlug(slug: String): Task[Option[Company]] = run {
    query[Company].filter(_.slug == lift(slug))
  }.map(_.headOption)

  override def get: Task[List[Company]] = run {
    query[Company]
  }
}
