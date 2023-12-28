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

  val badReview = Review(
    id = 2L,
    companyId = 1L,
    userId = 1L,
    management = 1,
    culture = 1,
    salary = 1,
    benefits = 1,
    wouldRecommend = 1,
    review = "bad bad",
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
    },
    test("get review by id, userid, companyid") {
      for {
        repo           <- ZIO.service[ReviewRepository]
        review         <- repo.create(goodReview)
        fetchedReview  <- repo.getById(review.id)
        fetchedReview2 <- repo.getByUserId(review.userId)
        fetchedReview3 <- repo.getByCompanyId(review.companyId)
      } yield assertTrue {
        fetchedReview.contains(review) &&
        fetchedReview2.contains(review) &&
        fetchedReview3.contains(review)
      }
    },
    test("get all") {
      for {
        repo         <- ZIO.service[ReviewRepository]
        review1      <- repo.create(goodReview)
        review2      <- repo.create(badReview)
        reviewByComp <- repo.getByCompanyId(1L)
        reviewByUser <- repo.getByUserId(1L)
      } yield assertTrue {
        reviewByComp.toSet == Set(review1, review2) &&
        reviewByUser.toSet == Set(review1, review2)
      }
    },
    test("modify review") {
      val newReview = "not too bad"
      for {
        repo    <- ZIO.service[ReviewRepository]
        review  <- repo.create(goodReview)
//        _       <- TestClock.adjust(2.seconds)
//        _       <- ZIO.sleep(2.seconds)
        updated <- repo.update(review.id, _.copy(review = newReview))
        _ <- ZIO.logWarning(s"original mtime: ${review.mtime}, modified time: ${updated.mtime}")
      } yield assertTrue {
        review.id == updated.id &&
        review.companyId == updated.companyId &&
        review.userId == updated.userId &&
        review.management == updated.management &&
        review.culture == updated.culture &&
        review.salary == updated.salary &&
        review.benefits == updated.benefits &&
        review.wouldRecommend == updated.wouldRecommend &&
        updated.review == newReview &&
        review.ctime == updated.ctime &&
        review.mtime != updated.mtime
      }
    },
    test("delete review") {
      for {
        repo        <- ZIO.service[ReviewRepository]
        review      <- repo.create(goodReview)
        _           <- repo.delete(review.id)
        maybeReview <- repo.getById(review.id)
      } yield assertTrue {
        maybeReview.isEmpty
      }
    }
  ).provide(
    ReviewRepositoryLive.layer,
    dataSourceLayer,
    Repository.quillLayer,
    Scope.default
  )

}
