package com.kaizen.api.services.movie

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie.repository.MovieRepository
import zio.random.Random
import zio.{Has, ZIO, ZLayer}
import zio.query.ZQuery

package object controller {
  type MovieController = Has[MovieController.Service]

  object MovieController {
    trait Service {
      def getMovie(id: MovieId): ZIO[Any, RepositoryError, MovieData]
      def addMovie(title: MovieTitle): ZIO[Random, RepositoryError, MovieData]
      def setMovieTitle(id: MovieId, title: MovieTitle): ZIO[Any, RepositoryError, MovieData]
      def removeMovie(id: MovieId): ZIO[Any, RepositoryError, Unit]
    }

    val live: ZLayer[MovieRepository, Nothing, MovieController] =
      ZLayer.fromService(new LiveMovieController(_))
  }

  def getMovie(id: MovieId): ZIO[MovieController, RepositoryError, MovieData] =
    ZIO.accessM[MovieController](_.get.getMovie(id))

  def addMovie(title: MovieTitle): ZIO[MovieController with Random, RepositoryError, MovieData] =
    ZIO.accessM[MovieController with Random](_.get.addMovie(title))

  def setMovieTitle(id: MovieId, title: MovieTitle): ZIO[MovieController, RepositoryError, MovieData] =
    ZIO.accessM[MovieController](_.get.setMovieTitle(id, title))

  def removeMovie(id: MovieId): ZIO[MovieController, RepositoryError, Unit] =
    ZIO.accessM[MovieController](_.get.removeMovie(id))
}
