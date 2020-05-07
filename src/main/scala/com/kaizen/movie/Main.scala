package com.kaizen.movie

import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder
import zio.interop.catz._
import zio.{App, RIO, ZEnv, ZIO}

import scala.concurrent.ExecutionContext.global

object Main extends App {
  val server: ZIO[ZEnv, Throwable, Unit] =
    ZIO.runtime[ZEnv].flatMap { implicit rte =>
      BlazeServerBuilder[RIO[ZEnv, *]](global)
        .withNio2(true)
        .bindHttp(8080, "localhost")
        .withHttpApp(HttpApp.notFound)
        .serve
        .compile
        .drain
    }

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    server
      .as(0)
      .orDie
}
