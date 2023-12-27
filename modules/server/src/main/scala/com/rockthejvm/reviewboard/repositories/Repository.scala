package com.rockthejvm.reviewboard.repositories

import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase

object Repository {

  private val quillLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)

  private val dataSourceLayer = Quill.DataSource.fromPrefix("rockthejvm.db")

  val dataLayer = dataSourceLayer >>> quillLayer
}
