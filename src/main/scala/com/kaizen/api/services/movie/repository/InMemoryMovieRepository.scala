package com.kaizen.api.services.movie.repository

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie._
import zio.stm.TMap
import zquery.DataSource

class InMemoryMovieRepository(memory: TMap[MovieId, MovieData]) extends MovieRepository.Impl {
  override val getById: DataSource[Any, GetMovieById] =
    DataSource.fromFunctionM("getMovieById") { getMovieById =>
      memory
        .get(getMovieById.id)
        .commit
        .someOrFail[MovieData, RepositoryError](RepositoryError.ItemNotFound(getMovieById.id))
    }

  override val update: DataSource[Any, UpdateMovie] =
    DataSource.fromFunctionM("updateMovie") { updateMovie =>
      memory
        .put(updateMovie.movie.id, updateMovie.movie)
        .commit
    }

  override val delete: DataSource[Any, DeleteMovie] =
    DataSource.fromFunctionM("deleteMovie") { deleteMovie =>
      memory
        .delete(deleteMovie.movie.id)
        .commit
    }
}
