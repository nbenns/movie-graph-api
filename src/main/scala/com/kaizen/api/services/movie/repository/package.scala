package com.kaizen.api.services.movie

import com.kaizen.api.services.RepositoryError
import zio._
import zio.stm.{TMap, ZSTM}

package object repository {
  type MovieRepository     = Has[MovieRepository.Service]

  object MovieRepository {
    trait Service {
      def getById(id: MovieId): ZSTM[Any, RepositoryError, MovieData]
      def update(movie: MovieData): ZSTM[Any, RepositoryError, Unit]
      def delete(movie: MovieData): ZSTM[Any, RepositoryError, Unit]
    }

    lazy val inMemory: ZLayer[Any, Nothing, MovieRepository] =
      TMap
        .empty[MovieId, MovieData]
        .map(new InMemoryMovieRepository(_))
        .commit
        .toLayer
  }

  def getMovieById(id: MovieId): ZSTM[MovieRepository, RepositoryError, MovieData] =
    for {
      hasMovieRepo <- ZSTM.environment[MovieRepository]
      movie        <- hasMovieRepo.get.getById(id)
    } yield movie

  def updateMovie(movie: MovieData): ZSTM[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZSTM.environment[MovieRepository]
      _            <- hasMovieRepo.get.update(movie)
    } yield ()

  def deleteMovie(movie: MovieData): ZSTM[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZSTM.environment[MovieRepository]
      _            <- hasMovieRepo.get.delete(movie)
    } yield ()
}
