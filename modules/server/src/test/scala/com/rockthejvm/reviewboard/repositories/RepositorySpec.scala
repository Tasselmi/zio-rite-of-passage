package com.rockthejvm.reviewboard.repositories

import zio.*
import zio.test.*
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource
import java.sql.SQLException
import org.postgresql.ds.PGSimpleDataSource

trait RepositorySpec {

  val initScript: String

  // test containers
  // spawn a Postgres instance on Docker just for the test
  private def createContainer() = {
    val container: PostgreSQLContainer[Nothing] = PostgreSQLContainer("postgres")
      .withInitScript(initScript)

    container.start()
    container
  }

  // create a datasource to connect to the Postgres
  private def createDataSource(container: PostgreSQLContainer[Nothing]) = {
    val ds = new PGSimpleDataSource()
    ds.setURL(container.getJdbcUrl)
    ds.setUser(container.getUsername)
    ds.setPassword(container.getPassword)
    ds.setDatabaseName("reviewboard")
    ds
  }

  // use the DataSource (as a ZLayer) to build the Quill instance (as a ZLayer)
  val dataSourceLayer = ZLayer {
    for {
      container <- ZIO.acquireRelease(ZIO.attempt(createContainer()))(c =>
        ZIO.attempt(c.stop()).ignoreLogged
      )
      ds <- ZIO.attempt(createDataSource(container))
    } yield ds
  }
}
