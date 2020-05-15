package com.kaizen.api.movie.repository

import com.kaizen.api.RepositoryError
import com.kaizen.api.movie._
import zio.stm.TMap
import zquery.DataSource

class InMemoryMovieRepository(memory: TMap[MovieId, Movie]) extends MovieRepository.Impl {
  override val getById: DataSource[Any, GetMovieById] =
    DataSource.fromFunctionM("getMovieById") { getMovieById =>
      memory
        .get(getMovieById.id)
        .commit
        .someOrFail[Movie, RepositoryError](RepositoryError.ItemNotFound(getMovieById.id))
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
