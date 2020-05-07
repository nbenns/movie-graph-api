package com.kaizen.movie

import caliban.Http4sAdapter
import zio.blocking.Blocking
import zio.interop.catz._
import zio.{App, Runtime, URIO, ZEnv, ZIO}

object Main extends App {
  override def run(args: List[String]): URIO[ZEnv, Int] = {
    for {
      implicit0(rte: Runtime[ZEnv]) <- ZIO.runtime[ZEnv]
      blockingEC                    <- ZIO.access[Blocking](_.get.blockingExecutor.asEC)
      graphqlInterpreter            <- graphql.api.interpreter.orDie
      graphqlRoute                   = Http4sAdapter.makeHttpService(graphqlInterpreter)
      _                             <- Http.server(blockingEC, graphqlRoute).orDie
    } yield 0
  }
}
