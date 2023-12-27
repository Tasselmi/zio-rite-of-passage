package com.rockthejvm.reviewboard

import com.rockthejvm.reviewboard.http.HttpApi
import com.rockthejvm.reviewboard.repositories.*
import com.rockthejvm.reviewboard.services.CompanyServiceLive
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio.*
import zio.http.Server

object Application extends ZIOAppDefault {

  val serverProgram = for {
    endpoints <- HttpApi.endpointZIO // contains controllers 
    _ <- Server.serve(ZioHttpInterpreter(ZioHttpServerOptions.default).toHttp(endpoints))
    _ <- Console.printLine("Server start! Go go go!")
  } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = serverProgram.provide(
    Server.default,
    //services
    CompanyServiceLive.layer,
    //repos
    CompanyRepositoryLive.layer,
    //other layers
    Repository.dataLayer // quill & db-connection
  )
}
