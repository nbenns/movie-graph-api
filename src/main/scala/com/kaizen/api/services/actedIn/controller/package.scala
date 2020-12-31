package com.kaizen.api.services.actedIn

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.repository.ActedInRepository
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.{Has, ZIO, ZLayer}

package object controller {
  type ActedInController = Has[ActedInController.Service]

  object ActedInController {
    trait Service {
      def getActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, ActedInData]
      def addActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, ActedInData]
      def removeActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, Unit]
      def getMoviesActedIn(actorId: ActorId): ZIO[Any, RepositoryError, List[ActedInData]]
      def countMoviesActedIn(actorId: ActorId): ZIO[Any, RepositoryError, Long]
    }

    val live: ZLayer[ActedInRepository, Nothing, ActedInController] =
      ZLayer.fromService(new LiveActedInController(_))
  }

  def getActedIn(actorId: ActorId, movieId: MovieId): ZIO[ActedInController, RepositoryError, ActedInData] =
    ZIO.accessM[ActedInController](_.get.getActedIn(actorId, movieId))

  def addActedIn(actorId: ActorId, movieId: MovieId): ZIO[ActedInController, RepositoryError, ActedInData] =
    ZIO.accessM[ActedInController](_.get.addActedIn(actorId, movieId))

  def removeActedIn(actorId: ActorId, movieId: MovieId): ZIO[ActedInController, RepositoryError, Unit] =
    ZIO.accessM[ActedInController](_.get.removeActedIn(actorId, movieId))

  def getMoviesActedIn(count: Long)(actorId: ActorId): ZIO[ActedInController, RepositoryError, List[ActedInData]] =
    ZIO.accessM[ActedInController](_.get.getMoviesActedIn(actorId))

  def countMoviesActedIn(actorId: ActorId): ZIO[ActedInController, RepositoryError, Long] =
    ZIO.accessM[ActedInController](_.get.countMoviesActedIn(actorId))
}
