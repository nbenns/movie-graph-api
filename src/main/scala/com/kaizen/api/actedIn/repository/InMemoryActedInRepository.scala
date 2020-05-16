package com.kaizen.api.actedIn.repository

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.ActedInData
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId
import zio.stm.TMap
import zquery.DataSource

class InMemoryActedInRepository(memory: TMap[(ActorId, MovieId), ActedInData]) extends ActedInRepository.Impl {

  override val getById: DataSource[Any, GetActedInById] =
    DataSource.fromFunctionM("getActedInById") { getActedIn =>
      memory
        .get((getActedIn.actorId, getActedIn.movieId))
        .commit
        .someOrFail[ActedInData, RepositoryError](RepositoryError.ItemNotFound((getActedIn.actorId, getActedIn.movieId)))
    }

  override val update: DataSource[Any, UpdateActedIn] =
    DataSource.fromFunctionM("updateActedIn") { updateActedIn =>
      val actorId = updateActedIn.actedIn.actorId
      val movieId = updateActedIn.actedIn.movieId
      val key = (actorId, movieId)

      memory
        .put(key, updateActedIn.actedIn)
        .commit
    }

  override val delete: DataSource[Any, DeleteActedIn] =
    DataSource.fromFunctionM("deleteActedIn") { deleteActedIn =>
      val actorId = deleteActedIn.actedIn.actorId
      val movieId = deleteActedIn.actedIn.movieId
      val key = (actorId, movieId)

      memory
        .delete(key)
        .commit
    }

  override val getMoviesActedIn: DataSource[Any, GetMoviesActedIn] =
    DataSource.fromFunctionM("getMoviesActedIn") { getMoviesActedIn =>
      memory
        .values
        .map(_.filter(_.actorId == getMoviesActedIn.actorId))
        .commit
    }
}
