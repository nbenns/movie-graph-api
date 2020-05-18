package com.kaizen.api

import caliban.Http4sAdapter
import com.kaizen.api.graphql.Schema
import com.kaizen.api.internal.actedIn.controller.ActedInController
import com.kaizen.api.internal.actedIn.repository.ActedInRepository
import com.kaizen.api.internal.actor.controller.ActorController
import com.kaizen.api.internal.actor.repository.ActorRepository
import com.kaizen.api.internal.movie.controller.MovieController
import com.kaizen.api.internal.movie.repository.MovieRepository
import zio.{App, Runtime, URIO, ZEnv, ZIO, ZLayer}
import zio.interop.catz._

object Main extends App {
  val dependencies: ZLayer[Any, Nothing, Controllers] =
    (MovieRepository.inMemory >>> MovieController.live) ++
      (ActorRepository.inMemory >>> ActorController.live) ++
      (ActedInRepository.inMemory >>> ActedInController.live)

  override def run(args: List[String]): URIO[ZEnv, Int] =
    (for {
      implicit0(rte: Runtime[ZAppEnv]) <- ZIO.runtime[ZAppEnv]

      httpExecutionContext = rte.platform.executor.asEC

      graphqlInterpreter <- Schema.api.interpreter

      graphqlInterpreterWithDep = graphqlInterpreter
      graphqlRoute = Http4sAdapter.makeHttpService(graphqlInterpreterWithDep)

      _ <- Http.server[ZApp](graphqlRoute, httpExecutionContext)
    } yield 0).provideCustomLayer(dependencies).orDie
}
