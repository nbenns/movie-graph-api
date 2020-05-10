package com.kaizen.api.movie

import com.kaizen.api.RepositoryError
import zio.stm.TMap
import zio._
import zquery.{DataSource, Request, ZQuery}

package object repository {
  type MovieRepository = Has[MovieRepository.Service]
  type MovieRepositoryImpl = Has[MovieRepository.Impl]

  final case class GetMovieById(id: MovieId) extends Request[RepositoryError, Movie]
  final case class UpdateMovie(movie: Movie) extends Request[RepositoryError, Unit]
  final case class DeleteMovie(movie: Movie) extends Request[RepositoryError, Unit]

  object MovieRepository {
    class Service(impl: Impl) {
      def getById(id: MovieId): ZQuery[Any, RepositoryError, Movie] =
        ZQuery.fromRequest(GetMovieById(id))(impl.getById)

      def update(movie: Movie): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(UpdateMovie(movie))(impl.update)

      def delete(movie: Movie): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(DeleteMovie(movie))(impl.delete)
    }

    private[repository] trait Impl {
      val getById: DataSource[Any, GetMovieById]
      val update:  DataSource[Any, UpdateMovie]
      val delete:  DataSource[Any, DeleteMovie]
    }

    private lazy val svc: ZLayer[MovieRepositoryImpl, Nothing, MovieRepository] = ZLayer.fromFunction(impl =>
      new Service(impl.get)
    )

    private lazy val inMemoryImpl: ZLayer[Any, Nothing, MovieRepositoryImpl] =
      ZLayer.fromEffect(
        TMap
          .empty[MovieId, Movie]
          .map(new InMemoryMovieRepository(_))
          .commit
      )

    lazy val inMemory: ZLayer[Any, Nothing, MovieRepository] = inMemoryImpl >>> svc
  }

  def getMovieById(id: MovieId): ZQuery[MovieRepository, RepositoryError, Movie] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      movie        <- hasMovieRepo.get.getById(id)
    } yield movie

  def updateMovie(movie: Movie): ZQuery[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      _            <- hasMovieRepo.get.update(movie)
    } yield ()

  def deleteMovie(movie: Movie): ZQuery[MovieRepository, RepositoryError, Unit] =
    for {
      hasMovieRepo <- ZQuery.environment[MovieRepository]
      _            <- hasMovieRepo.get.delete(movie)
    } yield ()
}
