package com.kaizen.api.actedIn

import com.kaizen.api.RepositoryError
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId
import zio.stm.TMap
import zio.{Has, ZLayer}
import zquery.{DataSource, Request, ZQuery}

package object repository {
  type ActedInRepository = Has[ActedInRepository.Service]
  type ActedInRepositoryImpl = Has[ActedInRepository.Impl]

  final case class GetActedInById(actorId: ActorId, movieId: MovieId) extends Request[RepositoryError, ActedIn]
  final case class UpdateActedIn(actedIn: ActedIn) extends Request[RepositoryError, Unit]
  final case class DeleteActedIn(actedIn: ActedIn) extends Request[RepositoryError, Unit]
  final case class GetMoviesActedIn(actorId: ActorId) extends Request[RepositoryError, List[ActedIn]]

  object ActedInRepository {
    class Service(impl: Impl) {
      def getActedInById(actorId: ActorId, movieId: MovieId): ZQuery[Any, RepositoryError, ActedIn] =
        ZQuery.fromRequest(GetActedInById(actorId, movieId))(impl.getById)

      def updateActedIn(actedIn: ActedIn): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(UpdateActedIn(actedIn))(impl.update)

      def deleteActedIn(actedIn: ActedIn): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(DeleteActedIn(actedIn))(impl.delete)

      def getMoviesActedIn(actorId: ActorId): ZQuery[Any, RepositoryError, List[ActedIn]] =
        ZQuery.fromRequest(GetMoviesActedIn(actorId))(impl.getMoviesActedIn)
    }

    private[repository] trait Impl {
      val getById: DataSource[Any, GetActedInById]
      val update: DataSource[Any, UpdateActedIn]
      val delete: DataSource[Any, DeleteActedIn]
      val getMoviesActedIn: DataSource[Any, GetMoviesActedIn]
    }

    private lazy val svc: ZLayer[ActedInRepositoryImpl, Nothing, ActedInRepository] =
      ZLayer.fromFunction(impl => new Service(impl.get))

    private lazy val inMemoryImpl: ZLayer[Any, Nothing, ActedInRepositoryImpl] =
      ZLayer.fromEffect(
        TMap
          .empty[(ActorId, MovieId), ActedIn]
          .map(new InMemoryActedInRepository(_))
          .commit
      )

    lazy val inMemory: ZLayer[Any, Nothing, ActedInRepository] = inMemoryImpl >>> svc
  }
}
