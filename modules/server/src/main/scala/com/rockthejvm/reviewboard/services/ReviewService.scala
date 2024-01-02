package com.rockthejvm.reviewboard.services

import java.time.Instant
import zio.*
import com.rockthejvm.reviewboard.repositories.ReviewRepository
import com.rockthejvm.reviewboard.http.requests.CreateReviewRequest
import com.rockthejvm.reviewboard.domain.data.Review


trait ReviewService {
  def create(request: CreateReviewRequest, userId: Long): Task[Review]
  def getById(id: Long): Task[Option[Review]]
  def getByCompanyId(cid: Long): Task[List[Review]]
  def getByUserId(uid: Long): Task[List[Review]]
}

class ReviewServiceLive private (repo: ReviewRepository) extends ReviewService {

  override def create(request: CreateReviewRequest, userId: Long): Task[Review] = repo.create(
    Review(
      id = 0L,
      companyId = request.companyId,
      userId = userId,
      management = request.management,
      culture = request.culture,
      salary = request.salary,
      benefits = request.benefits,
      wouldRecommend = request.wouldRecommend,
      review = request.review,
      ctime = Instant.now(),
      mtime = Instant.now()
    )
  )

  override def getById(id: Long): Task[Option[Review]] =
    repo.getById(id)

  override def getByCompanyId(cid: Long): Task[List[Review]] =
    repo.getByCompanyId(cid)

  override def getByUserId(uid: Long): Task[List[Review]] =
    getByUserId(uid)
}

object ReviewServiceLive {
  val layer: ZLayer[ReviewRepository, Nothing, ReviewServiceLive] = ZLayer {
    ZIO.serviceWith[ReviewRepository](rr => new ReviewServiceLive(rr))
  }
}
