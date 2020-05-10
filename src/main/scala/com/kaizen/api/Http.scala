package com.kaizen.api

import cats.data.Kleisli
import cats.effect.{ Blocker, ConcurrentEffect, ContextShift, Timer }
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{ HttpRoutes, StaticFile }

import scala.concurrent.ExecutionContext

object Http {
  def server[F[_]: ConcurrentEffect: ContextShift: Timer](
    gqlRoute: HttpRoutes[F],
    httpPool: ExecutionContext
  ): F[Unit] =
    (
      for {
        blocker <- Stream.resource(Blocker[F])

        routes = Router[F](
          "/api/graphql" -> gqlRoute,
          "/graphiql"    -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", blocker, None))
        )

        _ <- BlazeServerBuilder[F](httpPool)
              .withNio2(true)
              .bindHttp(8080, "localhost")
              .withHttpApp(routes.orNotFound)
              .serve
      } yield ()
    ).compile.drain
}
