package com.rockthejvm.reviewboard.syntax

import zio.*
import zio.test.*

extension [R, E, A](zio: ZIO[R, E, A]) {
  def assert(assertion: Assertion[A]) = assertZIO(zio)(assertion)
}
