package com.kaizen.api

import caliban.Http4sAdapter
import com.kaizen.api.actor.controller.ActorController
import com.kaizen.api.actor.repository.ActorRepository
import com.kaizen.api.movie.controller.MovieController
import com.kaizen.api.movie.repository.MovieRepository
import zio.interop.catz._
import zio.{ App, Runtime, URIO, ZEnv, ZIO, ZLayer }

object Main extends App {
  val dependencies: ZLayer[Any, Nothing, MovieController with ActorController] =
    (MovieRepository.inMemory >>> MovieController.live) ++
      (ActorRepository.inMemory >>> ActorController.live)

  type ZApp = ZEnv with MovieController with ActorController

  override def run(args: List[String]): URIO[ZEnv, Int] =
    (for {
      implicit0(rte: Runtime[ZApp]) <- ZIO.runtime[ZApp]

      httpExecutionContext = rte.platform.executor.asEC

      graphqlInterpreter <- Graphql.api.interpreter

      graphqlInterpreterWithDep = graphqlInterpreter
      graphqlRoute              = Http4sAdapter.makeHttpService(graphqlInterpreterWithDep)

      _ <- Http.server(graphqlRoute, httpExecutionContext)
    } yield 0).provideCustomLayer(dependencies).orDie
}
