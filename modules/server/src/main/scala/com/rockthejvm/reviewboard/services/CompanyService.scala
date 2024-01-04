package com.rockthejvm.reviewboard.services

import zio.*
import com.rockthejvm.reviewboard.domain.data.*
import com.rockthejvm.reviewboard.repositories.*
import com.rockthejvm.reviewboard.http.requests.*

// business logic
// in between the HTTP layer and the DB layer
// controller (http) ---> service (business) ---> repo (database)
trait CompanyService {
  def create(req: CreateCompanyRequest): Task[Company]
  def getAll: Task[List[Company]]
  def getById(id: Long): Task[Option[Company]]
  def getBySlug(slug: String): Task[Option[Company]]
}

class CompanyServiceLive private (repo: CompanyRepository) extends CompanyService {
  override def create(req: CreateCompanyRequest): Task[Company] =
    repo.create(req.toCompany(-1L))

  override def getAll: Task[List[Company]] =
    repo.get

  override def getById(id: Long): Task[Option[Company]] =
    repo.getById(id)

  override def getBySlug(slug: String): Task[Option[Company]] =
    repo.getBySlug(slug)
}

object CompanyServiceLive {
  val layer: ZLayer[CompanyRepository, Nothing, CompanyServiceLive] = ZLayer {
    for {
      repo <- ZIO.service[CompanyRepository]
    } yield new CompanyServiceLive(repo)
  }
}