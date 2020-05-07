package com.kaizen.movie

import caliban.{CalibanError, GraphQLInterpreter, Http4sAdapter}
import cats.Monad
import cats.data.Kleisli
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Sync, Timer}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpRoutes, StaticFile}
import org.http4s.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

object Http {
  def server[F[_]: ConcurrentEffect: ContextShift : Timer](blockingEC: ExecutionContext, gqlRoute: HttpRoutes[F]): F[Unit] = {
    val blocker = Blocker.liftExecutionContext(blockingEC)

    val routes = Router[F](
      "/api/graphql" -> gqlRoute,
      "/graphiql"    -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", blocker, None))
    )

    BlazeServerBuilder[F](global)
      .withNio2(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .serve
      .compile
      .drain
  }
}
