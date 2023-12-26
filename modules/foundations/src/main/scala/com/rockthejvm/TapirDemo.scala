package com.rockthejvm

import zio.*
import zio.http.Server
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}

import scala.collection.mutable

object TapirDemo extends ZIOAppDefault {

  val simplestEndpoint = endpoint
    .tag("simple")
    .name("simple")
    .description("simplest endpoint possible")
    .get
    .in("simple")
    .out(plainBody[String])
    .serverLogicSuccess[Task](_ => ZIO.succeed("all good"))

  val db: mutable.Map[Long, Job] = mutable.Map(
    1L -> Job(1L, "instructor", "rockthejvm.com", "rock the jvm")
  )

  val getAllEndpoint: ServerEndpoint[Any, Task] = endpoint
    .description("get all jobs")
    .in("jobs")
    .get
    .out(jsonBody[List[Job]])
    .serverLogicSuccess(_ => ZIO.succeed(db.values.toList))

  val createEndpoint: ServerEndpoint[Any, Task] = endpoint
    .description("create a job")
    .in("jobs")
    .post
    .in(jsonBody[CreateJobRequest])
    .out(jsonBody[Job])
    .serverLogicSuccess(cjr => ZIO.succeed {
      val newId = db.keys.max + 1
      val newJob = Job(newId, cjr.title, cjr.url, cjr.company)
      db += (newId -> newJob)
      newJob
    })

  val getByIdEndpoint: ServerEndpoint[Any, Task] = endpoint
    .description("get job by id")
    .in("jobs" / path[Long]("id"))
    .get
    .out(jsonBody[Option[Job]])
    .serverLogicSuccess(i => ZIO.succeed(db.get(i)))

  val simpleServerProgram = Server.serve(
    ZioHttpInterpreter(ZioHttpServerOptions.default)
      .toHttp(List(simplestEndpoint, getAllEndpoint, getByIdEndpoint, createEndpoint))
  )

  //http get localhost:8080/jobs
  //http get localhost:8080/jobs/1
  //http get localhost:8080/jobs/2
  //http post localhost:8080/jobs title='soft engineer' url='rockthejvm.com/zio' company='rock the jvm on zio'
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = simpleServerProgram.provide(
    Server.default
  )
}
