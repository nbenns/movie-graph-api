package com.kaizen.api

import caliban.Http4sAdapter
import com.kaizen.api.graphql.Schema
import com.kaizen.api.services.{actedIn, actor, movie}
import zio.interop.catz._
import zio._

object Main extends App {
  private val dependencies: ZLayer[Any, Nothing, Controllers] =
    movie.dependencies ++ actor.dependencies ++ actedIn.dependencies

  private def program: ZIO[ZAppEnv, Throwable, Unit] =
    for {
      implicit0(rte: Runtime[ZAppEnv]) <- ZIO.runtime[ZAppEnv]

      httpExecutionContext  = rte.platform.executor.asEC
      graphqlInterpreter   <- Schema.api.interpreter

      graphqlRoute          = Http4sAdapter.makeHttpService(graphqlInterpreter)
      _                    <- Http.server[ZApp](graphqlRoute, httpExecutionContext)
    } yield ()

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    program
      .provideCustomLayer(dependencies)
      .exitCode
}
