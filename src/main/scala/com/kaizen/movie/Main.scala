package com.kaizen.movie

import caliban.Http4sAdapter
import zio.interop.catz._
import zio.{App, Runtime, URIO, ZEnv, ZIO}

object Main extends App {
  override def run(args: List[String]): URIO[ZEnv, Int] = {
    for {
      implicit0(rte: Runtime[ZEnv]) <- ZIO.runtime[ZEnv]

      graphqlInterpreter            <- graphql.api.interpreter.orDie
      graphqlRoute                   = Http4sAdapter.makeHttpService(graphqlInterpreter)

      _                             <- Http.server(graphqlRoute).orDie
    } yield 0
  }
}
