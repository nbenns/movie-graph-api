package com.kaizen.api.internal.actedIn

import com.kaizen.api.internal.actor.ActorId
import com.kaizen.api.internal.RepositoryError
import com.kaizen.api.internal.movie.MovieId
import zio.stm.TMap
import zio.{Has, ZLayer}
import zquery.{DataSource, Request, ZQuery}

package object repository {
  type ActedInRepository = Has[ActedInRepository.Service]
  type ActedInRepositoryImpl = Has[ActedInRepository.Impl]

  final case class GetActedInById(actorId: ActorId, movieId: MovieId) extends Request[RepositoryError, ActedInData]
  final case class UpdateActedIn(actedIn: ActedInData) extends Request[RepositoryError, Unit]
  final case class DeleteActedIn(actedIn: ActedInData) extends Request[RepositoryError, Unit]
  final case class GetMoviesActedIn(actorId: ActorId) extends Request[RepositoryError, List[ActedInData]]
  final case class CountMoviesActedIn(actorId: ActorId) extends Request[RepositoryError, Long]

  object ActedInRepository {
    class Service(impl: Impl) {
      def getActedInById(actorId: ActorId, movieId: MovieId): ZQuery[Any, RepositoryError, ActedInData] =
        ZQuery.fromRequest(GetActedInById(actorId, movieId))(impl.getById)

      def updateActedIn(actedIn: ActedInData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(UpdateActedIn(actedIn))(impl.update)

      def deleteActedIn(actedIn: ActedInData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(DeleteActedIn(actedIn))(impl.delete)

      def getMoviesActedIn(actorId: ActorId): ZQuery[Any, RepositoryError, List[ActedInData]] =
        ZQuery.fromRequest(GetMoviesActedIn(actorId))(impl.getMoviesActedIn)

      def countMoviesActedIn(actorId: ActorId): ZQuery[Any, RepositoryError, Long] =
        ZQuery.fromRequest(CountMoviesActedIn(actorId))(impl.countMoviesActedIn)
    }

    private[repository] trait Impl {
      val getById: DataSource[Any, GetActedInById]
      val update: DataSource[Any, UpdateActedIn]
      val delete: DataSource[Any, DeleteActedIn]
      val getMoviesActedIn: DataSource[Any, GetMoviesActedIn]
      val countMoviesActedIn: DataSource[Any, CountMoviesActedIn]
    }

    private lazy val svc: ZLayer[ActedInRepositoryImpl, Nothing, ActedInRepository] =
      ZLayer.fromFunction(impl => new Service(impl.get))

    private lazy val inMemoryImpl: ZLayer[Any, Nothing, ActedInRepositoryImpl] =
      ZLayer.fromEffect(
        TMap
          .empty[(ActorId, MovieId), ActedInData]
          .map(new InMemoryActedInRepository(_))
          .commit
      )

    lazy val inMemory: ZLayer[Any, Nothing, ActedInRepository] = inMemoryImpl >>> svc
  }
}
