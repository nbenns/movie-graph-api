package com.kaizen.api.services.actedIn.repository

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.ActedInData
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.stm.{TMap, ZSTM}

class InMemoryActedInRepository(memory: TMap[(ActorId, MovieId), ActedInData]) extends ActedInRepository.Service {

  override def getById(actorId: ActorId, movieId: MovieId): ZSTM[Any, RepositoryError, ActedInData] =
      memory
        .get((actorId, movieId))
        .someOrFail[ActedInData, RepositoryError](RepositoryError.ItemNotFound((actorId, movieId)))

  override def update(actedInData: ActedInData): ZSTM[Any, Nothing, Unit] = {
      val actorId = actedInData.actorId
      val movieId = actedInData.movieId
      val key = (actorId, movieId)

      memory
        .put(key, actedInData)
    }

  override def delete(actedInData: ActedInData): ZSTM[Any, Nothing, Unit] = {
      val actorId = actedInData.actorId
      val movieId = actedInData.movieId
      val key = (actorId, movieId)

      memory
        .delete(key)
    }

  override def getMoviesActedIn(actorId: ActorId): ZSTM[Any, Nothing, List[ActedInData]] =
      memory
        .values
        .map(_.filter(_.actorId == actorId))

  override def countMoviesActedIn(actorId: ActorId): ZSTM[Any, Nothing, MovieId] =
      memory
        .values
        .map(_.filter(_.actorId == actorId))
        .map(_.length.toLong)
}
