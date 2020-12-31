package com.kaizen.api.services.movie.repository

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie._
import zio.stm.{TMap, ZSTM}

class InMemoryMovieRepository(memory: TMap[MovieId, MovieData]) extends MovieRepository.Service {
  override def getById(id: MovieId): ZSTM[Any, RepositoryError, MovieData] =
      memory
        .get(id)
        .someOrFail[MovieData, RepositoryError](RepositoryError.ItemNotFound(id))

  override def update(movie: MovieData): ZSTM[Any, RepositoryError, Unit] =
      memory
        .put(movie.id, movie)

  override def delete(movie: MovieData) =
      memory
        .delete(movie.id)
}
