package com.kaizen.api.graphql.datasource

import com.kaizen.api.graphql.{AddMovie, GetMovie, Movie, RemoveMovie, SetMovieTitle}
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie.{MovieId, MovieTitle}
import com.kaizen.api.services.movie.controller.MovieController
import zio.{Chunk, Has, ZIO}
import zio.query.{DataSource, ZQuery}
import zio.random.Random

object MovieDataSource {
  private val getMovieDS: DataSource[MovieController, GetMovie] =
    DataSource
      .fromFunctionM("GetMovie") { getMovie =>
        ZIO
          .accessM[MovieController](_.get.getMovie(getMovie.id))
          .map(Movie.fromMovieData)
      }

  private val addMovieDS: DataSource[MovieController with Random, AddMovie] =
    DataSource
      .fromFunctionM("AddMovie") { addMovie =>
        ZIO
          .accessM[MovieController with Random](_.get.addMovie(addMovie.title))
          .map(Movie.fromMovieData)
      }

  private val setMovieTitleDS: DataSource[MovieController, SetMovieTitle] =
    DataSource
      .fromFunctionM("SetMovieTitle") { setMovieTitle =>
        ZIO
          .accessM[MovieController](_.get.setMovieTitle(setMovieTitle.id, setMovieTitle.title))
          .map(Movie.fromMovieData)
      }

  private val removeMovieDS: DataSource[MovieController, RemoveMovie] =
    DataSource
      .fromFunctionM("RemoveMovie") { removeMovie =>
        ZIO
          .accessM[MovieController](_.get.removeMovie(removeMovie.id))
      }

  private val batchGetMoviesDS: DataSource[MovieController, GetMovie] =
    DataSource.fromFunctionBatchedM("BatchGetMovies") { chunk =>
      for {
        mc <- ZIO.service[MovieController.Service]
        out <- chunk.mapM(gm => mc.getMovie(gm.id))
      } yield out.map(Movie.fromMovieData)
    }

  def getMovie(getMovie: GetMovie): ZQuery[MovieController, RepositoryError, Movie] =
    ZQuery.fromRequest(getMovie)(getMovieDS)

  def getMovie(movieId: MovieId): ZQuery[MovieController, RepositoryError, Movie] =
    getMovie(GetMovie(movieId))


  def addMovie(addMovie: AddMovie): ZQuery[MovieController with Random, RepositoryError, Movie] =
    ZQuery.fromRequest(addMovie)(addMovieDS)

  def addMovie(title: MovieTitle): ZQuery[MovieController with Random, RepositoryError, Movie] =
    addMovie(AddMovie(title))


  def setMovieTitle(setMovieTitle: SetMovieTitle): ZQuery[MovieController, RepositoryError, Movie] =
    ZQuery.fromRequest(setMovieTitle)(setMovieTitleDS)

  def setMovieTitle(id: MovieId, title: MovieTitle): ZQuery[MovieController, RepositoryError, Movie] =
    setMovieTitle(SetMovieTitle(id, title))


  def removeMovie(removeMovie: RemoveMovie): ZQuery[MovieController, RepositoryError, Unit] =
    ZQuery.fromRequest(removeMovie)(removeMovieDS)

  def removeMovie(id: MovieId): ZQuery[MovieController, RepositoryError, Unit] =
    removeMovie(RemoveMovie(id))

  def batchGetMovies(ids: Chunk[GetMovie]): ZQuery[MovieController, RepositoryError, Movie] =
    ZQuery.fromRequest(ids(0))(batchGetMoviesDS)
}
