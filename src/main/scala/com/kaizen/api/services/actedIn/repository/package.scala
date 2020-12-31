package com.kaizen.api.services.actedIn

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.stm.{TMap, ZSTM}
import zio.{Has, ZLayer}

package object repository {
  type ActedInRepository = Has[ActedInRepository.Service]

  object ActedInRepository {
    trait Service {
      def getById(actorId: ActorId, movieId: MovieId): ZSTM[Any, RepositoryError, ActedInData]
      def update(actedInData: ActedInData): ZSTM[Any, RepositoryError, Unit]
      def delete(actedInData: ActedInData): ZSTM[Any, RepositoryError, Unit]
      def getMoviesActedIn(actorId: ActorId): ZSTM[Any, RepositoryError, List[ActedInData]]
      def countMoviesActedIn(actorId: ActorId): ZSTM[Any, RepositoryError, Long]
    }

    lazy val inMemory: ZLayer[Any, Nothing, ActedInRepository] =
      TMap
        .empty[(ActorId, MovieId), ActedInData]
        .map(new InMemoryActedInRepository(_))
        .commit
        .toLayer
  }
}
