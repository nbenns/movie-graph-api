package com.kaizen.api.internal.movie

import com.kaizen.api.internal.RepositoryError
import zio._
import zio.stm.TMap
import zquery.{DataSource, Request, ZQuery}

package object repository {
  type MovieRepository     = Has[MovieRepository.Service]
  type MovieRepositoryImpl = Has[MovieRepository.Impl]

  final case class GetMovieById(id: MovieId) extends Request[RepositoryError, MovieData]
  final case class UpdateMovie(movie: MovieData) extends Request[RepositoryError, Unit]
  final case class DeleteMovie(movie: MovieData) extends Request[RepositoryError, Unit]

  object MovieRepository {
    class Service(impl: Impl) {
      def getById(id: MovieId): ZQuery[Any, RepositoryError, MovieData] =
        ZQuery.fromRequest(GetMovieById(id))(impl.getById)

      def update(movie: MovieData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(UpdateMovie(movie))(impl.update)

      def delete(movie: MovieData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(DeleteMovie(movie))(impl.delete)
    }

    private[repository] trait Impl {
      val getById: DataSource[Any, GetMovieById]
      val update: DataSource[Any, UpdateMovie]
      val delete: DataSource[Any, DeleteMovie]
    }

    private lazy val svc: ZLayer[MovieRepositoryImpl, Nothing, MovieRepository] =
      ZLayer.fromFunction(impl => new Service(impl.get))

    private lazy val inMemoryImpl: ZLayer[Any, Nothing, MovieRepositoryImpl] =
      ZLayer.fromEffect(
        TMap
          .empty[MovieId, MovieData]
          .map(new InMemoryMovieRepository(_))
          .commit
      )

    lazy val inMemory: ZLayer[Any, Nothing, MovieRepository] = inMemoryImpl >>> svc
  }

  def getMovieById(id: MovieId): ZQuery[MovieRepository, RepositoryError, MovieData] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      movie        <- hasMovieRepo.get.getById(id)
    } yield movie

  def updateMovie(movie: MovieData): ZQuery[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      _            <- hasMovieRepo.get.update(movie)
    } yield ()

  def deleteMovie(movie: MovieData): ZQuery[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      _            <- hasMovieRepo.get.delete(movie)
    } yield ()
}
