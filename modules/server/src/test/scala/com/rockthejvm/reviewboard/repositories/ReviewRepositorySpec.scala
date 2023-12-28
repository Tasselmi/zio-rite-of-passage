package com.rockthejvm.reviewboard.repositories

import zio.*
import zio.test.*
import com.rockthejvm.reviewboard.domain.data.Review
import java.time.Instant
import com.rockthejvm.reviewboard.syntax.*

object ReviewRepositorySpec extends ZIOSpecDefault with RepositorySpec {

  val goodReview = Review(
    id = 1L,
    companyId = 1L,
    userId = 1L,
    management = 5,
    culture = 5,
    salary = 5,
    benefits = 5,
    wouldRecommend = 10,
    review = "all good",
    ctime = Instant.now(),
    mtime = Instant.now()
  )

  override val initScript: String = "sql/reviews.sql"

  override def spec: Spec[TestEnvironment with Scope, Any] = suite("ReviewRepositorySpec")(
    test("create review") {
      for {
        repo   <- ZIO.service[ReviewRepository]
        review <- repo.create(goodReview)
      } yield assertTrue {
        review.management == goodReview.management &&
        review.culture == goodReview.culture &&
        review.salary == goodReview.salary &&
        review.benefits == goodReview.benefits &&
        review.wouldRecommend == goodReview.wouldRecommend
      }
    }
  ).provide(
    ReviewRepositoryLive.layer,
    dataSourceLayer,
    Repository.quillLayer,
    Scope.default
  )

}
